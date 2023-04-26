/*
 * This file is part of hephaestus-engine, licensed under the MIT license
 *
 * Copyright (c) 2021-2023 Unnamed Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.lolivetgt.elitemodels.api.engine.view.animation;

import org.jetbrains.annotations.Nullable;

import com.lolivetgt.elitemodels.api.creativeapi.base.Vector3Float;
import com.lolivetgt.elitemodels.api.engine.Bone;
import com.lolivetgt.elitemodels.api.engine.animation.Animation;
import com.lolivetgt.elitemodels.api.engine.animation.KeyFrame;
import com.lolivetgt.elitemodels.api.engine.animation.Timeline;
import com.lolivetgt.elitemodels.api.engine.util.Vectors;
import com.lolivetgt.elitemodels.api.engine.view.BaseBoneView;
import com.lolivetgt.elitemodels.api.engine.view.BaseModelView;

import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

class NormalAnimationController implements AnimationController {

    private final Deque<Animation> queue = new LinkedList<>();
    private final BaseModelView<?> view;

    private final Map<String, KeyFrame> lastFrames = new HashMap<>();
    private final Map<String, Iterator<KeyFrame>> iterators = new HashMap<>();
    private int noNext;

    // Reference to the animation currently being played
    private @Nullable Animation current;

    NormalAnimationController(BaseModelView<?> view) {
        this.view = view;
    }

    @Override
    public synchronized void queue(Animation animation, int transitionTicks) {
        if (transitionTicks <= 0) {
            queue.addFirst(animation);
            nextAnimation();
            return;
        }

        Map<String, Timeline> framesByBone = new HashMap<>();

        lastFrames.forEach((boneName, frame) -> {
            Timeline keyFrames = framesByBone.computeIfAbsent(boneName, k -> Timeline.dynamic());
            keyFrames.put(0, Timeline.Channel.POSITION, frame.position());
            keyFrames.put(0, Timeline.Channel.ROTATION, frame.rotation());
            keyFrames.put(0, Timeline.Channel.SCALE, frame.scale());

            framesByBone.put(boneName, keyFrames);
        });

        animation.timelines().forEach((boneName, frames) -> {
            Iterator<KeyFrame> iterator = frames.iterator();
            if (iterator.hasNext()) {
                KeyFrame firstFrame = frames.iterator().next();

                Timeline keyFrames = framesByBone.computeIfAbsent(boneName, k -> Timeline.dynamic());
                keyFrames.put(transitionTicks, Timeline.Channel.POSITION, firstFrame.position());
                keyFrames.put(transitionTicks, Timeline.Channel.ROTATION, firstFrame.rotation());
                keyFrames.put(transitionTicks, Timeline.Channel.SCALE, firstFrame.scale());
            }
        });

        queue.addFirst(new Animation(
                "generated-transition",
                Animation.LoopMode.HOLD,
                framesByBone
        ));

        queue.addFirst(animation);
        nextAnimation();
    }

    private void updateBone(
            double yaw,
            Bone parent,
            Bone bone,
            Vector3Float parentRotation,
            Vector3Float parentPosition
    ) {

        BaseBoneView boneView = view.bone(bone.name());
        assert boneView != null;

        Vector3Float globalPosition;
        Vector3Float globalRotation;

        KeyFrame frame = nextFrame(bone.name());
        Vector3Float framePosition = frame.position();
        Vector3Float frameRotation = frame.rotation();

        Vector3Float defaultPosition = bone.position();
        Vector3Float defaultRotation = bone.rotation();

        Vector3Float localPosition = framePosition.add(defaultPosition);
        Vector3Float localRotation = defaultRotation.add(frameRotation);

        if (parent == null) {
            globalPosition = Vectors.rotateAroundY(localPosition, yaw);
            globalRotation = localRotation;
        } else {
            globalPosition = Vectors.rotateAroundY(
                    Vectors.rotate(localPosition, parentRotation),
                    yaw
            ).add(parentPosition);
            globalRotation = Vectors.combineRotations(localRotation, parentRotation);
        }

        // TODO: Ugly design, why does it just return rotation? Should we use mutable positions/rotations?
        globalRotation = updateBonePositionAndRotation(boneView, globalPosition, globalRotation);

        for (Bone child : bone.children()) {
            updateBone(
                    yaw,
                    bone,
                    child,
                    globalRotation,
                    globalPosition
            );
        }
    }

    protected Vector3Float updateBonePositionAndRotation(
            BaseBoneView boneView,
            Vector3Float position,
            Vector3Float rotation
    ) {
        boneView.position(position);
        boneView.rotation(rotation);
        return rotation;
    }

    @Override
    public void clearQueue() {
        queue.clear();
        current = null;
    }

    @Override
    public synchronized void tick(double yaw) {
        for (Bone bone : view.model().bones()) {
            updateBone(
                    yaw,
                    null,
                    bone,
                    Vector3Float.ZERO,
                    Vector3Float.ZERO
            );
        }
    }

    private void nextAnimation() {
        if ((current = queue.pollLast()) != null) {
            createIterators(current);
        }
    }

    private void createIterators(Animation animation) {
        iterators.clear();
        animation.timelines().forEach((name, list) ->
                iterators.put(name, list.iterator()));
    }

    private KeyFrame nextFrame(String boneName) {

        if (current == null) {
            // if no animation currently being played,
            // try poll one from the animation queue
            nextAnimation();
            if (current == null) {
                // if queue was empty, the last frame or
                // the initial keyframe is returned
                return fallback(boneName);
            }
        }

        Iterator<KeyFrame> iterator = iterators.get(boneName);

        if (iterator != null && iterator.hasNext()) {
            // if there are more key frames for current
            // bone name
            KeyFrame frame = iterator.next();
            lastFrames.put(boneName, frame);

            if (iterator.hasNext()) {
                // if current bone animation iterator did not
                // finish, use the current frame
                return frame;
            } else if (++noNext >= iterators.size()) {
                // all iterators fully consumed, this means
                // the animation finished
                noNext = 0;
                switch (current.loopMode()) {
                    case ONCE:
                        nextAnimation();
                        // animation ended, lastFrames are removed
                        // so that next calls will return INITIAL
                        lastFrames.remove(boneName);
                        return frame;
                    case LOOP:
                        createIterators(current);
                        return frame;
                    case HOLD:
                        nextAnimation();
                        return frame;
                }
            }
        }

        return fallback(boneName);
    }

    private KeyFrame fallback(String boneName) {
        return lastFrames.getOrDefault(boneName, KeyFrame.INITIAL);
    }

}

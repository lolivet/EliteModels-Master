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

import com.lolivetgt.elitemodels.api.creativeapi.base.Vector3Float;
import com.lolivetgt.elitemodels.api.engine.view.BaseBoneView;
import com.lolivetgt.elitemodels.api.engine.view.BaseModelView;

import java.util.HashMap;
import java.util.Map;

final class DetailedAnimationController extends NormalAnimationController {


    private final Map<String, Vector3Float> lastRotations = new HashMap<>();
    private final Map<String, Vector3Float> halfRotations = new HashMap<>();

    private boolean update = true;

    DetailedAnimationController(BaseModelView<?> view) {
        super(view);
    }

    @Override
    protected Vector3Float updateBonePositionAndRotation(
            BaseBoneView boneView,
            Vector3Float position,
            Vector3Float rotation
    ) {
        String boneName = boneView.bone().name();

        if (update) {

            Vector3Float lastRotation = lastRotations.getOrDefault(boneName, Vector3Float.ZERO);

            // From: https://github.com/WorldSeedGames/WorldSeedEntityEngine/blob/master/src/main/java/net/worldseed/multipart/ModelBonePartArmourStandNoInterpolation.java#L78
            // By iam4722202468
            Vector3Float halfStep = rotation.subtract(lastRotation);

            float halfStepX = halfStep.x() % 360;
            float halfStepY = halfStep.y() % 360;
            float halfStepZ = halfStep.z() % 360;

            if (halfStepX > 180) halfStepX -= 360;
            if (halfStepX < -180) halfStepX += 360;

            if (halfStepY > 180) halfStepY -= 360;
            if (halfStepY < -180) halfStepY += 360;

            if (halfStepZ > 180) halfStepZ -= 360;
            if (halfStepZ < -180) halfStepZ += 360;

            float divisor = 2F;
            halfRotations.put(
                    boneName,
                    lastRotation.add(new Vector3Float(halfStepX / divisor, halfStepY / divisor, halfStepZ / divisor))
            );

            boneView.position(position);
            boneView.rotation(lastRotation);
            lastRotations.put(boneName, rotation);
        } else {
            boneView.rotation(halfRotations.get(boneName));
            rotation = halfRotations.get(boneName);
        }

        return rotation;
    }

    @Override
    public synchronized void tick(double yaw) {
        super.tick(yaw);
        update = !update;
    }

}

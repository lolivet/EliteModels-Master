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
package com.lolivetgt.elitemodels.api.engine;

import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;

import com.lolivetgt.elitemodels.api.creativeapi.base.Vector3Float;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Represents a {@link Model} movable part, in the
 * game, it has its own Creative's {@link com.lolivetgt.elitemodels.api.creativeapi.model.Model}
 * and its own armor_stand entity
 *
 * @since 1.0.0
 */
public class Bone implements Examinable {

    private final String name;

    private final Vector3Float position;
    private final Vector3Float rotation;

    private final Map<String, Bone> children;

    private final boolean small;
    private final int customModelData;

    public Bone(
            String name,
            Vector3Float position,
            Vector3Float rotation,
            Map<String, Bone> children,
            boolean small,
            int customModelData
    ) {
        this.name = name;
        this.position = position;
        this.rotation = rotation;
        this.children = children;
        this.small = small;
        this.customModelData = customModelData;
    }

    /**
     * Returns this bone unique name, bone names
     * are unique in the {@link Model} scope
     *
     * @return The bone name
     */
    public String name() {
        return name;
    }

    /**
     * Returns the bone position relative to parent
     * bone position, in Minecraft blocks
     *
     * @return The bone position
     */
    public Vector3Float position() {
        return position;
    }

    /**
     * Returns this bone initial rotation
     *
     * @return The bone initial rotation
     */
    public Vector3Float rotation() {
        return rotation;
    }

    /**
     * Returns this bone custom model data,
     * which must be applied to the creative
     * Model representing this bone
     *
     * <p>Consider this number as a "handle"
     * or "connection" to the resource-pack
     * model information</p>
     *
     * <strong>This number must be unique in
     * the resource-pack</strong>
     *
     * @return The bone custom model data
     */
    public int customModelData() {
        return customModelData;
    }

    /**
     * Determines whether to use small armor stands
     * for this bone
     *
     * @return True to use small armor stands
     */
    public boolean small() {
        return small;
    }

    /**
     * Returns this bone child bones
     *
     * @return The child bones
     */
    public Collection<Bone> children() {
        return children.values();
    }

    /**
     * Returns a map of this bone children
     * bones, keys are bone names
     *
     * @return The child bone map
     */
    public Map<String, Bone> childrenMap() {
        return children;
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("name", name),
                ExaminableProperty.of("rotation", rotation),
                ExaminableProperty.of("bones", children),
                ExaminableProperty.of("offset", position),
                ExaminableProperty.of("small", small),
                ExaminableProperty.of("customModelData", customModelData)
        );
    }

    @Override
    public String toString() {
        return examine(StringExaminer.simpleEscaping());
    }

}
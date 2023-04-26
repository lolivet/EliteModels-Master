/*
 * This file is part of creative, licensed under the MIT license
 *
 * Copyright (c) 2021-2022 Unnamed Team
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
package com.lolivetgt.elitemodels.api.creativeapi.model;

import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.lolivetgt.elitemodels.api.creativeapi.base.CubeFace;
import com.lolivetgt.elitemodels.api.creativeapi.base.Vector3Float;
import com.lolivetgt.elitemodels.api.creativeapi.base.Vector4Float;
import com.lolivetgt.elitemodels.api.creativeapi.util.Validate;

import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * Defines the properties of a {@link CubeFace}
 * from a {@link Element}
 *
 * @since 1.0.0
 */
public class ElementFace implements Examinable {

    public static final float MINECRAFT_UV_UNIT = 16F;

    public static final int DEFAULT_ROTATION = 0;
    public static final int DEFAULT_TINT_INDEX = -1;

    @Nullable private final Vector4Float uv;
    private final String texture;
    @Nullable private final CubeFace cullFace;
    private final int rotation;
    private final int tintIndex;

    private ElementFace(
            @Nullable Vector4Float uv,
            String texture,
            @Nullable CubeFace cullFace,
            int rotation,
            int tintIndex
    ) {
        this.uv = uv;
        this.texture = requireNonNull(texture, "texture");
        this.cullFace = cullFace;
        this.rotation = rotation;
        this.tintIndex = tintIndex;
        validate();
    }

    private void validateUVComponentInRange(float value, String name) {
        Validate.isTrue(value >= 0 && value <= 1,
                "UV component '%s' is not within zero and one", name);
    }

    private void validate() {
        Validate.isTrue(rotation % 90 == 0 && rotation >= 0 && rotation <= 270,
                "Rotation must be a positive multiple of 90");
        if (uv != null) {
            validateUVComponentInRange(uv.x(), "X (from)");
            validateUVComponentInRange(uv.y(), "Y (from)");
            validateUVComponentInRange(uv.x2(), "X (to)");
            validateUVComponentInRange(uv.y2(), "Y (to)");
        }
    }

    /**
     * Returns the area of the texture to
     * use according to the scheme [x1, y1, x2, y2]
     *
     * <p>All components are always between 0 and
     * 1 and then converted to 0-16 (Minecraft UV)
     * when creating the resource pack</p>
     *
     * <p>If the numbers of x1 and x2 are swapped
     * the texture flips</p>
     *
     * <p>UV is optional, and if not supplied it automatically
     * generates based on the element's position.</p>
     *
     * @return The texture area to use
     */
    public @Nullable Vector4Float uv() {
        return uv;
    }

    /**
     * Returns this face's texture in variable
     * form prepended with a "#"
     *
     * @return The face texture
     */
    public String texture() {
        return texture;
    }

    /**
     * Returns whether a face does not need to be rendered
     * when there is a block touching it in the specified
     * position. It also determines the side of the block to
     * use the light level from for lighting the face
     *
     * @return The element cull face
     */
    public @Nullable CubeFace cullFace() {
        return cullFace;
    }

    /**
     * Returns this face texture rotation, it rotates the texture
     * by the specified number of degrees. Can be 0, 90, 180, or
     * 270.
     *
     * <p>Rotation does not affect which part of the texture is
     * used. Instead, it amounts to permutation of the selected
     * texture vertexes (selected implicitly, or explicitly though
     * uv)</p>
     *
     * @return The face texture rotation
     */
    public int rotation() {
        return rotation;
    }

    /**
     * Determines whether to tint the texture using a hardcoded tint
     * index.
     *
     * @return The face tint index
     */
    public int tintIndex() {
        return tintIndex;
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("uv", uv),
                ExaminableProperty.of("texture", texture),
                ExaminableProperty.of("cullFace", cullFace),
                ExaminableProperty.of("rotation", rotation),
                ExaminableProperty.of("tintIndex", tintIndex)
        );
    }

    @Override
    public String toString() {
        return examine(StringExaminer.simpleEscaping());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElementFace that = (ElementFace) o;
        return rotation == that.rotation
                && Objects.equals(uv, that.uv)
                && texture.equals(that.texture)
                && cullFace == that.cullFace
                && tintIndex == that.tintIndex;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uv, texture, cullFace, rotation, tintIndex);
    }

    /**
     * Creates a new {@link ElementFace} instance from
     * the provided values
     *
     * @param uv The texture area to use, will be automatically
     *           generated by the client if not set
     * @param texture The texture name
     * @param cullFace The element face cull face
     * @param rotation The face rotation, must be multiple
     *                 of 90 and be less than 360
     * @param tintIndex The face tint index
     * @return The recently created {@link ElementFace} instance
     * @since 1.0.0
     */
    public static ElementFace of(
            @Nullable Vector4Float uv,
            String texture,
            @Nullable CubeFace cullFace,
            int rotation,
            int tintIndex
    ) {
        return new ElementFace(uv, texture, cullFace, rotation, tintIndex);
    }

    public static Vector4Float getDefaultUvForFace(
            CubeFace face,
            Vector3Float from,
            Vector3Float to
    ) {
        switch (face) {
            case WEST:
                return new Vector4Float(from.z(), 1F - to.y(), to.z(), 1F - from.y());
            case EAST:
                return new Vector4Float(1F - to.z(), 1F - to.y(), 1F - from.z(), 1F - from.y());
            case DOWN:
                return new Vector4Float(from.x(), 1F - to.z(), to.x(), 1F - from.z());
            case UP:
                return new Vector4Float(from.x(), from.z(), to.x(), to.z());
            case NORTH:
                return new Vector4Float(1F - to.x(), 1F - to.y(), 1F - from.x(), 1F - from.y());
            case SOUTH:
                return new Vector4Float(from.x(), 1F - to.y(), to.x(), 1F - from.y());
            default:
                throw new IllegalArgumentException("Unknown face: " + face);
        }
    }

    /**
     * Static factory method for creating a new
     * instance of our builder implementation,
     * which eases the creation of {@link ElementFace}
     * instances
     *
     * @return A new builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder implementation for creating
     * {@link ElementFace} instances
     *
     * @since 1.0.0
     */
    public static class Builder {

        private Vector4Float uv;
        private String texture;
        private CubeFace cullFace;
        private int rotation = DEFAULT_ROTATION;
        private int tintIndex = DEFAULT_TINT_INDEX;

        private Builder() {
        }

        public Builder uv(@Nullable Vector4Float uv) {
            this.uv = uv;
            return this;
        }

        public Builder texture(String texture) {
            this.texture = requireNonNull(texture, "texture");
            return this;
        }

        public Builder cullFace(@Nullable CubeFace cullFace) {
            this.cullFace = cullFace;
            return this;
        }

        public Builder rotation(int rotation) {
            this.rotation = rotation;
            return this;
        }

        public Builder tintIndex(int tintIndex) {
            this.tintIndex = tintIndex;
            return this;
        }

        /**
         * Finishes building the {@link ElementFace}
         * instance with the previously set values
         *
         * @return The face instance
         */
        public ElementFace build() {
            return new ElementFace(uv, texture, cullFace, rotation, tintIndex);
        }

    }

}

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
package com.lolivetgt.elitemodels.api.creativeapi.base;

import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Represents a fixed-size vector of three 32-bit
 * floating-point numbers, immutable
 *
 * @since 1.0.0
 */
public final class Vector3Float
        implements Examinable, Iterable<Float> {

    /**
     * Constant for {@link Vector3Float} value with
     * abscissa, ordinate and aplicate of {@code 0} zero.
     */
    public static final Vector3Float ZERO = new Vector3Float(0F, 0F, 0F);

    /**
     * Constant for {@link Vector3Float} value with
     * abscissa, ordinate and aplicate of {@code 1} one.
     */
    public static final Vector3Float ONE = new Vector3Float(1F, 1F, 1F);

    private final float x;
    private final float y;
    private final float z;

    public Vector3Float(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Returns the "X" component or abscissa
     * of this vector
     *
     * @return The vector abscissa
     */
    public float x() {
        return x;
    }

    /**
     * Returns the "Y" component or ordinate
     * of this vector
     *
     * @return The vector ordinate
     */
    public float y() {
        return y;
    }

    /**
     * Returns the "Z" component or applicate
     * of this vector
     *
     * @return The vector applicate
     */
    public float z() {
        return z;
    }

    public Vector3Float add(float x, float y, float z) {
        return new Vector3Float(this.x + x, this.y + y, this.z + z);
    }

    public Vector3Float subtract(float x, float y, float z) {
        return new Vector3Float(
                this.x - x,
                this.y - y,
                this.z - z
        );
    }

    public Vector3Float divide(float value) {
        return new Vector3Float(
                this.x / value,
                this.y / value,
                this.z / value
        );
    }

    public Vector3Float divide(float x, float y, float z) {
        return new Vector3Float(
                this.x / x,
                this.y / y,
                this.z / z
        );
    }

    public float dot(Vector3Float vector) {
        return this.x * vector.x() + this.y * vector.y() + this.z * vector.z();
    }

    public Vector3Float crossProduct(Vector3Float o) {
        return new Vector3Float(
                y * o.z - o.y * z,
                z * o.x - o.z * x,
                x * o.y - o.x * y
        );
    }

    public Vector3Float multiply(float value) {
        return new Vector3Float(
                this.x * value,
                this.y * value,
                this.z * value
        );
    }

    public Vector3Float multiply(float x, float y, float z) {
        return new Vector3Float(
                this.x * x,
                this.y * y,
                this.z * z
        );
    }

    public Vector3Float add(Vector3Float value) {
        return add(value.x(), value.y(), value.z());
    }

    public Vector3Float subtract(Vector3Float value) {
        return subtract(value.x(), value.y(), value.z());
    }

    public Vector3Float multiply(Vector3Float value) {
        return multiply(value.x(), value.y(), value.z());
    }

    public Vector3Float x(float x) {
        return new Vector3Float(x, this.y, this.z);
    }

    public Vector3Float y(float y) {
        return new Vector3Float(this.x, y, this.z);
    }

    public Vector3Float z(float z) {
        return new Vector3Float(this.x, this.y, z);
    }

    public Vector3Float with(Axis3D axis, float value) {
        float x = this.x;
        float y = this.y;
        float z = this.z;
        switch (axis) {
            case X:
                x = value;
                break;
            case Y:
                y = value;
                break;
            case Z:
                z = value;
                break;
            default:
                throw new IllegalArgumentException("Invalid axis: " + axis);
        }
        return new Vector3Float(x, y, z);
    }

    public float[] toArray() {
        return new float[] { x, y, z };
    }

    @NotNull
    @Override
    public Iterator<Float> iterator() {
        return Arrays.asList(x, y, z).iterator();
    }

    /**
     * Returns the component at a specific {@link Axis3D},
     * fails if axis is invalid
     *
     * @param axis The requested axis
     * @return The component at the given axis
     * @throws IllegalArgumentException If axis is not X,
     * Y or Z
     */
    public float get(Axis3D axis) {
        switch (axis) {
            case X:
                return x;
            case Y:
                return y;
            case Z:
                return z;
            default:
                throw new IllegalArgumentException(
                        "Invalid axis: " + axis
                );
        }
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("x", x),
                ExaminableProperty.of("y", y),
                ExaminableProperty.of("z", z)
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
        Vector3Float that = (Vector3Float) o;
        return x == that.x
                && y == that.y
                && z == that.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

}

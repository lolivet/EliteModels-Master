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

/**
 * Enum of (three-dimensional) cubes faces (6),
 * can be positive or negative {@link Axis3D#X},
 * {@link Axis3D#Y} or {@link Axis3D#Z}
 *
 * @since 1.0.0
 */
public enum CubeFace {

    WEST(Axis3D.X, -1),
    EAST(Axis3D.X, 1),
    DOWN(Axis3D.Y, -1),
    UP(Axis3D.Y, 1),
    NORTH(Axis3D.Z, -1),
    SOUTH(Axis3D.Z, 1);

    private final Axis3D axis;
    private final int factor;

    CubeFace(Axis3D axis, int factor) {
        this.axis = axis;
        this.factor = factor;
    }

    /**
     * Returns the axis of this face, X for
     * west and east, Y for down and up, and
     * Z for north and south
     *
     * @return The face axis
     * @since 1.0.0
     */
    public Axis3D axis() {
        return axis;
    }

    /**
     * Returns the factor of this face direction,
     * 1 for positive directions, -1 for negative
     * directions
     *
     * @return The face factor
     * @since 1.0.0
     */
    public int factor() {
        return factor;
    }

}

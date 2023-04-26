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
package com.lolivetgt.elitemodels.api.engine.view.track;

import com.lolivetgt.elitemodels.api.engine.view.BaseModelView;

/**
 * Functional interface that determines whether a viewer candidate
 * should actually be added as a model's viewer
 *
 * @param <TViewer> The viewer/player type, depends on platform
 * @since 1.0.0
 */
@FunctionalInterface
public interface ModelViewTrackingRule<TViewer> {

    /**
     * Function called every time a new viewer candidate is found,
     * commonly, when it enters the model's vision range
     *
     * @param view The model view
     * @param candidate The viewer candidate
     * @return True if the candidate should be added as viewer
     */
    boolean shouldView(BaseModelView<TViewer> view, TViewer candidate);

    /**
     * A tracking rule that lets <strong>all</strong> the players
     * see the model
     *
     * @return A {@link ModelViewTrackingRule} instance that always returns true
     * @param <TViewer> The viewer/player type, depends on platform
     */
    static <TViewer> ModelViewTrackingRule<TViewer> all() {
        return (view, candidate) -> true;
    }

}

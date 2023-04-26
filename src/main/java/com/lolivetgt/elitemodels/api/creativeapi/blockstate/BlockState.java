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
package com.lolivetgt.elitemodels.api.creativeapi.blockstate;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;
import com.lolivetgt.elitemodels.api.creativeapi.file.ResourceWriter;
import com.lolivetgt.elitemodels.api.creativeapi.file.FileResource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * There are several variants of some blocks (like doors, which can be
 * open or closed), hence each block has its own block state file, which
 * lists all its existing variants and links them to their corresponding
 * models
 *
 * <p>Blocks can also be compound of several models at the same time,
 * called "multipart". The models are then used depending on the block
 * states of the block</p>
 *
 * @since 1.0.0
 */
public class BlockState implements Keyed, FileResource {

    private final Key key;
    private final Map<String, MultiVariant> variants;
    private final List<Selector> multipart;

    private BlockState(
            Key key,
            Map<String, MultiVariant> variants,
            List<Selector> multipart
    ) {
        this.key = requireNonNull(key, "key");
        this.variants = requireNonNull(variants, "variants");
        this.multipart = requireNonNull(multipart, "multipart");
        validate();
    }

    private void validate() {
        if (variants.isEmpty() && multipart.isEmpty()) {
            throw new IllegalArgumentException("variants and multipart cannot be both empty!");
        }
    }

    @Override
    public @NotNull Key key() {
        return key;
    }

    public Map<String, MultiVariant> variants() {
        return variants;
    }

    public List<Selector> multipart() {
        return multipart;
    }

    @Override
    public String path() {
        return "assets/" + key.namespace() + "/blockstates/" + key.value() + ".json";
    }

    @Override
    public void serialize(ResourceWriter writer) {

        writer.startObject();

        // write "variants" part if not empty
        if (!variants.isEmpty()) {
            writer.key("variants").startObject();
            for (Map.Entry<String, MultiVariant> entry : variants.entrySet()) {
                writer.key(entry.getKey())
                        .value(entry.getValue());
            }
            writer.endObject();
        }

        // write "multipart" part if not empty
        if (!multipart.isEmpty()) {
            writer.key("multipart").startArray();
            for (Selector selector : multipart) {
                selector.serialize(writer);
            }
            writer.endArray();
        }

        writer.endObject();
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("key", key),
                ExaminableProperty.of("variants", variants),
                ExaminableProperty.of("multipart", multipart)
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
        BlockState that = (BlockState) o;
        return key.equals(that.key)
                && variants.equals(that.variants)
                && multipart.equals(that.multipart);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, variants, multipart);
    }

    /**
     * Creates a new {@link BlockState} object from
     * the given values
     *
     * @param variants The block state variants
     * @param multipart The block state variants
     *                  (multipart)
     * @return A new {@link BlockState} instance
     * @since 1.0.0
     */
    public static BlockState of(
            Key key,
            Map<String, MultiVariant> variants,
            List<Selector> multipart
    ) {
        return new BlockState(key, variants, multipart);
    }

    /**
     * Creates a new {@link BlockState} instance with
     * the given block state variants
     *
     * @param variants The block state variants
     * @return A new {@link BlockState} variants
     * @since 1.0.0
     */
    public static BlockState of(Key key, Map<String, MultiVariant> variants) {
        return new BlockState(
                key,
                variants,
                Collections.emptyList()
        );
    }

    /**
     * Creates a new {@link BlockState} instance from
     * the given block state multipart info
     *
     * @param multipart The block state cases to use
     * @return A new {@link BlockState} instance
     * @since 1.0.0
     */
    public static BlockState of(Key key, List<Selector> multipart) {
        return new BlockState(
                key,
                Collections.emptyMap(),
                multipart
        );
    }

    public static BlockState of(Key key, Selector... multipart) {
        return new BlockState(
                key,
                Collections.emptyMap(),
                Arrays.asList(multipart)
        );
    }

}

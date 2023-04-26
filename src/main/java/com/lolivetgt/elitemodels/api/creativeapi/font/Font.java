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
package com.lolivetgt.elitemodels.api.creativeapi.font;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;
import com.lolivetgt.elitemodels.api.creativeapi.file.ResourceWriter;
import com.lolivetgt.elitemodels.api.creativeapi.file.FileResource;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * Represents a resource-pack font file, located
 * at assets/&lt;namespace&gt;/font and is compound
 * by a list of font providers ({@link FontProvider}) that
 * tie a character set to a resource location along
 * with some extra information
 *
 * <p>The default font is defined by "minecraft:default"
 * font ({@link Font#MINECRAFT_DEFAULT}) while
 * the default font used by enchantment tables is defined
 * by the {@link Font#MINECRAFT_ALT}</p>
 *
 * @since 1.0.0
 */
public class Font implements Keyed, FileResource {

    public static final Key MINECRAFT_DEFAULT = Key.key(Key.MINECRAFT_NAMESPACE, "default");
    public static final Key MINECRAFT_ALT = Key.key(Key.MINECRAFT_NAMESPACE, "alt");
    public static final Key MINECRAFT_ILAGERALT = Key.key(Key.MINECRAFT_NAMESPACE, "ilageralt");
    public static final Key MINECRAFT_UNIFORM = Key.key(Key.MINECRAFT_NAMESPACE, "uniform");

    private final Key key;
    private final List<FontProvider> providers;

    private Font(
            Key key,
            List<FontProvider> providers
    ) {
        this.key = requireNonNull(key, "key");
        this.providers = requireNonNull(providers, "providers");
    }

    @Override
    public @NotNull Key key() {
        return key;
    }

    /**
     * Returns a list of font providers
     * that are merged onto this font
     *
     * @return The font providers
     * @since 1.0.0
     */
    public List<FontProvider> providers() {
        return providers;
    }

    @Override
    public String path() {
        return "assets/" + key.namespace() + "/font/" + key.value() + ".json";
    }

    @Override
    public void serialize(ResourceWriter writer) {
        writer.startObject()
                .key("providers").value(providers)
                .endObject();
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("key", key),
                ExaminableProperty.of("providers", providers)
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Font that = (Font) o;
        return key.equals(that.key)
                && providers.equals(that.providers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, providers);
    }

    @Override
    public String toString() {
        return examine(StringExaminer.simpleEscaping());
    }

    /**
     * Creates a new {@link Font} instance from
     * the given provider list
     *
     * @param key The font key
     * @param providers The font providers
     * @return A new {@link Font} instance
     * @since 1.0.0
     */
    public static Font of(Key key, List<FontProvider> providers) {
        return new Font(key, providers);
    }

    /**
     * Creates a new {@link Font} instance from
     * the given providers
     *
     * @param key The font key
     * @param providers The font providers
     * @return A new {@link Font} instance
     * @since 1.0.0
     */
    public static Font of(Key key, FontProvider... providers) {
        return new Font(key, Arrays.asList(providers));
    }

}

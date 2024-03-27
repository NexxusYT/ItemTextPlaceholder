package me.nex.itp.controller;

import me.nex.itp.params.CharacterVisitorParams;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Implementation of {@link PlaceholderController} providing functionality to manage placeholder parameters and definitions.
 */
public class PlaceholderControllerImpl implements PlaceholderController {

    private final MatrixStack matrixStack = new MatrixStack();

    private final List<CharacterVisitorParams> params = new ArrayList<>();
    private final AtomicBoolean placeholderDefinition = new AtomicBoolean();
    private final AtomicReference<Float> offset = new AtomicReference<>();

    @Override
    public MatrixStack getMatrixStack() {
        return this.matrixStack;
    }

    @Override
    public @Unmodifiable Collection<CharacterVisitorParams> getParams() {
        return this.params.stream().toList();
    }

    @Override
    public void addParam(CharacterVisitorParams param) {
        this.params.add(param);
    }

    @Override
    public void clearParams() {
        this.params.clear();
    }

    @Override
    public boolean isPlaceholderDefinition() {
        return this.placeholderDefinition.get();
    }

    @Override
    public void setPlaceholderDefinition(boolean placeholderDefinition) {
        this.placeholderDefinition.set(placeholderDefinition);
    }

    @Override
    public float getOffset() {
        return this.offset.get();
    }

    @Override
    public void setOffset(float offset) {
        this.offset.set(offset);
    }

    @Override
    public void addOffset(float offset) {
        this.offset.updateAndGet(v -> v + offset);
    }
}

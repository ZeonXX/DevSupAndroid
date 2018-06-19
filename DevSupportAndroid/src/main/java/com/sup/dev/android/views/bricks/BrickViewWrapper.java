package com.sup.dev.android.views.bricks;

public interface BrickViewWrapper {

    <K extends BrickViewWrapper>K hide();

    <K extends BrickViewWrapper>K update();

    <K extends BrickViewWrapper>K setCancelable(boolean cancelable);

    <K extends BrickViewWrapper>K setEnabled(boolean enabled);

}

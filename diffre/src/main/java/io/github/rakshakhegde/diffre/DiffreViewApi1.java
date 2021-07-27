package io.github.rakshakhegde.diffre;


import ohos.agp.components.AttrSet;
import ohos.agp.render.Region;
import ohos.agp.utils.Rect;
import ohos.app.Context;
import org.jetbrains.annotations.Nullable;

/**
 * Works with any background. No anti-aliasing for this approach.
 * Created by rakshakhegde on 16/02/17.
 */
public class DiffreViewApi1 extends DiffreView {

    final Region textRegion = new Region();
    final Region progressRegion = new Region();
    final Region region = new Region();

    public DiffreViewApi1(Context context) {
        this(context, null);
    }

    public DiffreViewApi1(Context context, @Nullable AttrSet attrs) {
        this(context, attrs, 0);
    }

    public DiffreViewApi1(Context context, @Nullable AttrSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void computeCroppedProgressPath() {
        region.setRect(new Rect(0, 0, (int) (width * percent), height));

        // NOTE Below operations are not working as expected
        progressRegion.setPath(progressStrokePath, region); // INTERSECT
        textRegion.setPath(textPath, region);

        progressRegion.op(textRegion, Region.Op.DIFFERENCE); // DIFFERENCE

        croppedProgressPath.rewind();
        progressRegion.getBoundaryPath(croppedProgressPath);
    }

    @Override
    public void computeCroppedTextPath() {
        region.setRect(new Rect((int) (width * percent), 0, width, height));

        // NOTE Below operation is not working as expected
        textRegion.setPath(textPath, region); // INTERSECT

        croppedTextPath.rewind();
        textRegion.getBoundaryPath(croppedTextPath);
    }
}

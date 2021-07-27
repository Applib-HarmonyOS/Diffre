package io.github.rakshakhegde.diffre;


import ohos.agp.components.AttrHelper;
import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.render.Path;
import ohos.agp.utils.Color;
import ohos.agp.utils.Rect;
import ohos.agp.utils.RectFloat;
import ohos.agp.utils.TextAlignment;
import ohos.app.Context;
import org.jetbrains.annotations.Nullable;

/**
 * Works with any background. Also refer to concrete implementation {@link DiffreViewApi1}
 */
public abstract class DiffreView extends Component implements Component.EstimateSizeListener, Component.DrawTask {

    int width;
    int height;
    float percent = 0.1F;
    Path progressStrokePath = new Path();

    final Path textPath = new Path();
    final Path croppedProgressPath = new Path();
    final Path croppedTextPath = new Path();

    private static final RectFloat rectF = new RectFloat();

    // Display parameters in fp
    private static final int PAINT_TEXT_SIZE = 18;

    // Display parameters in vp
    private static final int PAINT_TEXT_PADDING = 18;
    private static final int PAINT_RADIUS = 10;

    // Display parameters in px
    public static final float STROKE_WIDTH = 4f;

    // Display Color
    private static final int DISPLAY_COLOR = 0xFFFD9727;

    // Display Text
    private static final String TEXT_STRING = "16:00 – 16:30";
    // Above parameters may be modified to customize the display

    private final Paint paint = new Paint();
    private final float radius;
    private final int textPadding;
    private final Rect textBounds = new Rect();


    protected DiffreView(Context context) {
        this(context, null);
    }

    protected DiffreView(Context context, @Nullable AttrSet attrs) {
        this(context, attrs, 0);
    }

    protected DiffreView(Context context, @Nullable AttrSet attrs, int defStyleId) {
        super(context, attrs, defStyleId);


        final int curPaintTextSize = AttrHelper.fp2px(PAINT_TEXT_SIZE, context);
        paint.setTextSize(curPaintTextSize);

        textPadding = AttrHelper.vp2px(PAINT_TEXT_PADDING, context);

        radius = AttrHelper.vp2px(PAINT_RADIUS, context);

        initPaint();

        setEstimateSizeListener(this);
        addDrawTask(this);
    }

    /**
     * Creates a Path object to hold a Rounded Rectangle of specified dimensions in the Clockwise drawing direction.
     *
     * @param left - Indicates the X coordinate of the upper left corner of the specified rounded rectangle.
     * @param top - Indicates the Y coordinate of the upper left corner of the specified rounded rectangle.
     * @param right - Indicates the X coordinate of the lower right corner of the specified rounded rectangle.
     * @param bottom - Indicates the Y coordinate of the lower right corner of the specified rounded rectangle
     * @param radius - The radius of the rounded corners of the rectangle.
     * @return A Path object representing the specified rounded rectangle.
     */
    public static Path getRoundRectPath(float left, float top, float right, float bottom, float radius) {
        Path roundRectPath = new Path();
        rectF.modify(left, top, right, bottom);
        roundRectPath.addRoundRect(rectF, radius, radius, Path.Direction.CLOCK_WISE);
        return roundRectPath;
    }

    /**
     * Sets to the path object a clockwise directional rectangle constructed with the given directional parameters.
     *
     * @param path The path object to hold the rectangular path.
     * @param left - Indicates the X coordinate of the upper left corner of the specified rectangle.
     * @param top - Indicates the Y coordinate of the upper left corner of the specified rectangle.
     * @param right - Indicates the X coordinate of the lower right corner of the specified rectangle.
     * @param bottom - Indicates the Y coordinate of the lower right corner of the specified rectangle
     */
    public static void setRectPath(Path path, float left, float top, float right, float bottom) {
        rectF.modify(left, top, right, bottom);
        path.rewind();
        path.addRect(rectF, Path.Direction.CLOCK_WISE);
    }

    private void initPaint() {
        paint.setAntiAlias(true);
        paint.setTextAlign(TextAlignment.CENTER);

        paint.setStrokeWidth(STROKE_WIDTH);
    }

    @Override
    public boolean onEstimateSize(int widthMeasureSpec, int heightMeasureSpec) {
        textBounds.modify(paint.getTextBounds(TEXT_STRING));

        final int textWidth = textBounds.getWidth();
        final int textHeight = textBounds.getHeight();

        width = textWidth + textPadding;
        height = textHeight + textPadding;

        final int cx = width / 2;
        final int cy = (height + textHeight) / 2;

        paint.addTextToPath(TEXT_STRING, cx, cy, textPath);
        progressStrokePath = getRoundRectPath(0, 0, width, height, radius);

        computePaths();

        // Adding 1 to prevent artifacts
        setEstimatedSize(Component.EstimateSpec.getChildSizeWithMode(width + 1, width + 1, EstimateSpec.NOT_EXCEED),
                Component.EstimateSpec.getChildSizeWithMode(height + 1, height + 1, EstimateSpec.NOT_EXCEED));
        return true;
    }

    @Override
    public void onDraw(Component component, Canvas canvas) {

        paint.setColor(new Color(DISPLAY_COLOR));
        paint.setStyle(Paint.Style.STROKE_STYLE);

        canvas.drawPath(progressStrokePath, paint);

        paint.setStyle(Paint.Style.FILL_STYLE);
        canvas.drawPath(croppedProgressPath, paint);
        canvas.drawPath(croppedTextPath, paint);
    }

    /**
     * Sets the progress of this component and redraws the component.
     *
     * @param fraction - A float value between 0.0 and 1.0 indicating progress, i.e. the amount of text of this
     *                component that is filled.
     */
    public void setProgress(final float fraction) {
        if (!(fraction >= 0F && fraction <= 1F)) {
            throw new IllegalArgumentException("Progress can only be set to a value between 0.0 and 1.0");
        }
        percent = fraction;
        computePaths();
        invalidate();
    }

    private void computePaths() {
        computeCroppedProgressPath();
        computeCroppedTextPath();
    }

    public abstract void computeCroppedProgressPath();

    public abstract void computeCroppedTextPath();

}

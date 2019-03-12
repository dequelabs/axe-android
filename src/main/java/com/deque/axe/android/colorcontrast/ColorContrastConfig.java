package com.deque.axe.android.colorcontrast;

public class ColorContrastConfig {

  /**
   * The minimum number of times we expect to transition from background color to text color
   * to be sufficiently satisfied that we have found a line of text AND a potentially Text
   * and Background color pair.
   */
  static final int MIN_CHARACTERS = 5;

  /**
   * To Handle Anti-aliasing, we do a combination of considering true color equality and also
   * visible color equality. Examples: #FFFFFF and #F6F6F6 are visibly the same color. High
   * value for this will lead to more confidence when overall contrast is reasonably high, however
   * may also lead to misses on occasions when the text and background colors are very close.
   * Low values for this will lead to more results, but lower confidence values.
   */
  static final double EQUAL_COLORS_CONTRAST_THRESHOLD = 1.1;

  /**
   * At some point we need to give up on the idea that given transition might be text. This value
   * configures the maximum plausible thickness of a font for a given screen, though in practice
   * this can be much larger than that and still be effective. There is little value in making this
   * value as close to this threshold as possible. In practice we're really just filtering out large
   * icons and borders from things we consider "Text".
   */
  static final int MAX_TEXT_THICKNESS = 100;

  /**
   * If a given pair of colors occurs this many times more frequently than other color combinations
   * we are sure it is the Text/Background color pair. Otherwise, there are too many potential
   * combinations in a valueY for us to be positive about any given pair.
   */
  static final int TRANSITION_COUNT_DOMINANCE_FACTOR = 2;

}

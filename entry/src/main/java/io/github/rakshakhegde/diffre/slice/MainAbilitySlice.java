/*
 * Copyright (C) 2020-21 Application Library Engineering Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.rakshakhegde.diffre.slice;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Slider;
import io.github.rakshakhegde.diffre.DiffreView;
import io.github.rakshakhegde.diffre.ResourceTable;

/**
 * This ability slice displays a progress text bar along with a freely adjustable slider to set the progress of the
 * text.
 */
public class MainAbilitySlice extends AbilitySlice {

    Slider seekbar;
    DiffreView diffreViewApi1;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);

        seekbar = (Slider) findComponentById(ResourceTable.Id_seekBar);
        diffreViewApi1 = (DiffreView) findComponentById(ResourceTable.Id_fillShapeViewApi1);

        if (diffreViewApi1 != null) {
            float progress;
            if (seekbar != null) {
                progress = seekbar.getProgress();
            } else {
                progress = 60F;
            }
            diffreViewApi1.setProgress(progress / 100F);
        }
        if (seekbar != null) {
            seekbar.setValueChangedListener(new Slider.ValueChangedListener() {
                @Override
                public void onProgressUpdated(Slider seekBar, int progress, boolean fromUser) {
                    diffreViewApi1.setProgress(progress / 100F);
                }

                @Override
                public void onTouchStart(Slider seekBar) {
                    // Left empty because this functionality is not required.
                }

                @Override
                public void onTouchEnd(Slider seekBar) {
                    // Left empty because this functionality is not required.
                }
            });
        }
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}

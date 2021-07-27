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

package io.github.rakshakhegde.diffre;

import io.github.rakshakhegde.diffre.DiffreView;
import io.github.rakshakhegde.diffre.DiffreViewApi1;
import ohos.aafwk.ability.delegation.AbilityDelegatorRegistry;
import ohos.agp.components.Attr;
import ohos.agp.components.AttrSet;
import ohos.app.Context;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TestDiffreViewApi1 {
    Context context = AbilityDelegatorRegistry.getAbilityDelegator().getAppContext();
    AttrSet attrSet = new AttrSet() {
        @Override
        public Optional<String> getStyle() {
            return Optional.empty();
        }

        @Override
        public int getLength() {
            return 0;
        }

        @Override
        public Optional<Attr> getAttr(int i) {
            return Optional.empty();
        }

        @Override
        public Optional<Attr> getAttr(String s) {
            return Optional.empty();
        }
    };
    @Test
    public void testInitBasic() {
        DiffreViewApi1 diffreViewApi1 = new DiffreViewApi1(context);
        assertNotNull(diffreViewApi1);
    }
    @Test
    public void testInitWithAttrs() {
        DiffreViewApi1 diffreViewApi1 = new DiffreViewApi1(context, attrSet);
        assertNotNull(diffreViewApi1);
    }
    @Test
    public void testOnEstimateSize() {
        // Internally calls computePaths(), which calls computeCroppedProgressPath()
        // and computeCroppedTextPath()

        DiffreView diffreView = new DiffreViewApi1(context, attrSet);
        diffreView.setProgress(0.5F);
        assertTrue(diffreView.onEstimateSize(100, 100));
    }
}

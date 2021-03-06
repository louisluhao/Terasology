/*
 * Copyright 2011 Benjamin Glatzel <benjamin.glatzel@me.com>.
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
package org.terasology.rendering.gui.components;

import org.lwjgl.opengl.GL11;
import org.terasology.game.Terasology;
import org.terasology.model.inventory.Icon;
import org.terasology.model.inventory.Inventory;
import org.terasology.model.inventory.Item;
import org.terasology.rendering.gui.framework.UIDisplayElement;
import org.terasology.rendering.gui.framework.UIGraphicsElement;

import javax.vecmath.Vector2f;

import static org.lwjgl.opengl.GL11.*;

/**
 * A single cell of the toolbar with a small text label and a selection
 * rectangle.
 *
 * @author Benjamin Glatzel <benjamin.glatzel@me.com>
 */
public class UIToolbarCell extends UIDisplayElement {

    private final UIGraphicsElement _selectionRectangle;
    private final UIText _label;

    private int _id;
    private boolean _selected = false;

    public UIToolbarCell(int id) {
        _id = id;

        setSize(new Vector2f(48f, 48f));

        _selectionRectangle = new UIGraphicsElement("gui");
        _selectionRectangle.getTextureSize().set(new Vector2f(24f / 256f, 24f / 256f));
        _selectionRectangle.getTextureOrigin().set(new Vector2f(0.0f, 24f / 256f));
        _selectionRectangle.setSize(new Vector2f(48f, 48f));

        _label = new UIText();
        _label.setVisible(true);
        _label.setPosition(new Vector2f(30f, 20f));
    }

    @Override
    public void update() {
        if (Terasology.getInstance().getActiveWorldRenderer() == null)
            return;

        _selectionRectangle.setVisible(_selected);
        setPosition(new Vector2f((getSize().x - 8f) * _id - 2f, 2f));

        Inventory inventory = Terasology.getInstance().getActiveWorldRenderer().getPlayer().getInventory();

        if (inventory.getSelectedCubbyhole() == _id) {
            setSelected(true);
        } else {
            setSelected(false);
        }

        displayItemCount();
    }

    private void displayItemCount() {
        Inventory inventory = Terasology.getInstance().getActiveWorldRenderer().getPlayer().getInventory();
        int count = inventory.getItemCountAt(_id);

        if (count == 0) {
            getLabel().setVisible(false);
        } else {
            getLabel().setVisible(true);
            getLabel().setText(Integer.toString(count));
        }
    }

    @Override
    public void render() {
        if (Terasology.getInstance().getActiveWorldRenderer() == null)
            return;

        _selectionRectangle.renderTransformed();

        Inventory inventory = Terasology.getInstance().getActiveWorldRenderer().getPlayer().getInventory();
        Item item = inventory.getItemAt(_id);

        glEnable(GL11.GL_DEPTH_TEST);

        if (item != null) {
            glClear(GL11.GL_DEPTH_BUFFER_BIT);
            glPushMatrix();
            glTranslatef(20f, 20f, 0f);
            Icon.get(item).render();
            glPopMatrix();
        }

        glDisable(GL11.GL_DEPTH_TEST);

        _label.renderTransformed();
    }

    public void setSelected(boolean selected) {
        _selected = selected;
    }

    public boolean getSelected() {
        return _selected;
    }

    public UIText getLabel() {
        return _label;
    }
}

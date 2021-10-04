package com.deco2800.game.components.npc;

import com.deco2800.game.components.Component;
import com.deco2800.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to an entity's state and plays the animation when one
 * of the events is triggered.
 */
public class ProjectileAnimationController extends Component {
    AnimationRenderComponent animator;
    private boolean death;
    private boolean animateCall;

    @Override
    public void create() {
        super.create();
        death = false;
        animateCall = false;
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("brokenArrow", this::broken);
    }


    public void broken() {
        if (!entity.getEntityType().equals("fireBall")) {
            animator.startAnimation("brokenArrow");
        } else {
            animator.startAnimation("hit");
        }
    }

}

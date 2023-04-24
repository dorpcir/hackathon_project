package org.academiadecodigo.hackathon.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import org.academiadecodigo.hackathon.Cave;

public class B2WorldCreator {
    public B2WorldCreator(World world, TiledMap map){
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        for(MapObject object: map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / Cave.PPM, (rect.getY() + rect.getHeight() / 2) / Cave.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / Cave.PPM, rect.getHeight() / 2 / Cave.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }
        for(MapObject object: map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / Cave.PPM, (rect.getY() + rect.getHeight() / 2) / Cave.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / Cave.PPM, rect.getHeight() / 2 / Cave.PPM);
            fdef.shape = shape;
            Fixture fixture = body.createFixture(fdef);
            fixture.setUserData("deadly");
        }
        for(MapObject object: map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / Cave.PPM, (rect.getY() + rect.getHeight() / 2) / Cave.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / Cave.PPM, rect.getHeight() / 2 / Cave.PPM);
            fdef.shape = shape;
            fdef.isSensor = true; // Set fixture as a sensor
            Fixture fixture = body.createFixture(fdef);
            fixture.setUserData("win"); // Set user data for the fixture
        }
    }
}

package net.turrem.viewer;

import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL11;

import org.lwjgl.util.glu.GLU;

import org.lwjgl.BufferUtils;

public class RenderViewer
{
	private FloatBuffer lightPosition;
	private FloatBuffer whiteLight;
	private FloatBuffer specLight;
	private FloatBuffer lModelAmbient;
	
	private boolean mat = true;

	private PlayerFace face;
	
	private Viewer theViewer;

	public RenderViewer(Viewer viewer)
	{
		this.face = new PlayerFace();
		this.theViewer = viewer;
	}

	public void render()
	{
		this.face.tickCamera();

		GL11.glClearColor(0.18F, 0.18F, 0.23F, 1.0F);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glPushMatrix();

		this.doPerspective(this.getFace());
		this.renderModel();

		GL11.glPopMatrix();

		GL11.glPushMatrix();

		this.doOrtho();
		this.renderInterface();

		GL11.glPopMatrix();
	}

	public void renderInterface()
	{

	}

	public void renderModel()
	{
		this.face.doGLULook();
		this.doLighting();
		this.theViewer.render();
	}

	private void initLightArrays()
	{
		this.lightPosition = BufferUtils.createFloatBuffer(4);
		this.lightPosition.put(5.0f).put(10.0f).put(8.0f).put(0.0f).flip();

		this.whiteLight = BufferUtils.createFloatBuffer(4);
		this.whiteLight.put(0.5f).put(0.5f).put(0.5f).put(0.75f).flip();

		this.specLight = BufferUtils.createFloatBuffer(4);
		this.specLight.put(0.1f).put(0.1f).put(0.1f).put(1.0f).flip();

		this.lModelAmbient = BufferUtils.createFloatBuffer(4);
		this.lModelAmbient.put(0.5f).put(0.5f).put(0.5f).put(1.75f).flip();
	}

	public void doPerspective(PlayerFace face)
	{
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective(face.getFOVY(), face.getAspect(), face.getZNear(), 256.0F);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

	public void doOrtho()
	{
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, Config.getWidth(), Config.getHeight(), 0, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		GL11.glColor3f(1.0F, 1.0F, 1.0F);
	}

	public void doLighting()
	{
		this.initLightArrays();
		GL11.glShadeModel(GL11.GL_SMOOTH);

		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, this.lightPosition);
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_SPECULAR, this.specLight);
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, this.whiteLight);
		GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, this.lModelAmbient);

		GL11.glEnable(GL11.GL_LIGHT0);

		if (this.mat)
		{
			GL11.glEnable(GL11.GL_COLOR_MATERIAL);
			GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT_AND_DIFFUSE);
		}
		else
		{
			GL11.glDisable(GL11.GL_COLOR_MATERIAL);
		}
	}

	public PlayerFace getFace()
	{
		return face;
	}
}

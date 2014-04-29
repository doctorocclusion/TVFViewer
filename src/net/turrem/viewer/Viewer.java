package net.turrem.viewer;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;

import javax.imageio.ImageIO;

import net.turrem.utils.graphics.ImgUtils;
import net.turrem.utils.models.TVFFile;
import net.turrem.utils.models.VOXFile;
import net.turrem.viewer.render.engine.RenderEngine;
import net.turrem.viewer.render.object.RenderObject;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;

public class Viewer
{
	public String modelFile;
	public String theDir;
	public RenderViewer render;
	public RenderObject tvfRender;
	
	public long tickCount = 0;

	public Viewer(String filename, String rundir)
	{
		this.theDir = rundir;
		this.modelFile = filename;
	}
	
	public void run()
	{
		this.updateDisplay(Config.getWidth(), Config.getHeight(), false);

		Keyboard.enableRepeatEvents(false);

		this.render = new RenderViewer(this);
		
		/*
		try
		{
			this.setIcons();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		*/

		try
		{
			Display.create();
		}
		catch (LWJGLException e)
		{
			e.printStackTrace();
		}

		this.runloop();
	}
	
	public void runloop()
	{
		while (!Display.isCloseRequested())
		{
			try
			{
				this.tick();
			}
			catch (Exception e)
			{
				e.printStackTrace();
				this.shutdown();
				return;
			}

			Display.update();
			Display.sync(60);
		}
		this.shutdown();
	}

	private void tick()
	{
		this.tickCount++;
		if (this.tickCount == 4)
		{
			if (this.modelFile != null)
			{
				File file = new File(this.modelFile);
				this.loadFile(file);
			}
		}
		this.render.render();
	}

	public void render()
	{
		if (this.tvfRender != null)
		{
			this.tvfRender.doRender();
		}
	}
	
	public void setDisplayMode(int width, int height)
	{
		if ((Display.getDisplayMode().getWidth() == width) && (Display.getDisplayMode().getHeight() == height))
		{
			return;
		}

		try
		{
			DisplayMode targetDisplayMode = new DisplayMode(width, height);
			Display.setFullscreen(false);
			Display.setDisplayMode(targetDisplayMode);
		}
		catch (LWJGLException e)
		{
			System.err.println("Unable to setup mode " + width + "x" + height + e);
		}
	}

	public void shutdown()
	{
		Display.destroy();
		System.exit(0);
	}

	public void updateDisplay(int width, int height, boolean vsync)
	{
		this.setDisplayMode(width, height);
		Display.setTitle("TVF Viewer");
		Display.setVSyncEnabled(vsync);
	}

	public void setIcons() throws IOException
	{
		ArrayList<ByteBuffer> icos = new ArrayList<ByteBuffer>();

		File folder = new File(this.theDir + "assets/core/icons/");

		File[] filelist = folder.listFiles();

		for (File icon : filelist)
		{
			BufferedImage img = ImageIO.read(icon);
			icos.add(ImgUtils.imgToByteBuffer(img));
		}

		Display.setIcon(icos.toArray(new ByteBuffer[0]));
	}

	protected boolean loadFile(File file)
	{
		if (file.exists() && file.isFile())
		{
			this.modelFile = file.getPath();
			String ext = this.getExtension(file.getName());
			switch(ext)
			{
				case "tvf":
					return this.loadTVF(file);
				case "vox":
					return this.loadVox(file);
				default:
					return false;
			}
		}
		return false;
	}

	public String getExtension(String file)
	{
		String extension = "";

		int i = file.lastIndexOf('.');
		if (i > 0)
		{
			extension = file.substring(i + 1);
		}
		
		return extension;
	}

	protected boolean loadTVF(File file)
	{
		try
		{
			return this.doLoadTVF(file);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}
	}

	protected boolean loadVox(File file)
	{
		try
		{
			return this.doLoadVox(file);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	protected boolean doLoadTVF(File file) throws IOException
	{
		DataInputStream input = new DataInputStream(new GZIPInputStream(new FileInputStream(file)));
		TVFFile tvf = TVFFile.read(input);
		input.close();
		if (tvf == null)
		{
			return false;
		}
		this.tvfRender = RenderEngine.makeObject(tvf, 1.0F, (tvf.width & 0xFF) / 2, (tvf.height & 0xFF) / 2, (tvf.length & 0xFF) / 2);
		return this.tvfRender != null;
	}

	protected boolean doLoadVox(File file) throws IOException
	{
		DataInputStream input = new DataInputStream(new FileInputStream(file));
		VOXFile vox = VOXFile.read(input);
		input.close();
		if (vox == null)
		{
			return false;
		}
		TVFFile tvf = TVFFile.convertVox(vox);
		if (tvf == null)
		{
			return false;
		}
		this.tvfRender = RenderEngine.makeObject(tvf, 1.0F, (tvf.width & 0xFF) / 2, (tvf.height & 0xFF) / 2, (tvf.length & 0xFF) / 2);
		return this.tvfRender != null;
	}
}

package net.turrem.viewer;

import java.io.File;
import java.io.IOException;

public class Viewer
{
	public String modelFile;

	public Viewer(String filename)
	{
		if (filename != null)
		{
			File file = new File(filename);
			this.loadFile(file);
		}
	}
	
	public void run()
	{
		
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
		return false;
	}

	protected boolean doLoadVox(File file) throws IOException
	{
		return false;
	}
}

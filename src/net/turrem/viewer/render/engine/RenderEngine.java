package net.turrem.viewer.render.engine;

import net.turrem.viewer.render.object.RenderObject;
import net.turrem.viewer.render.object.model.TVFBuffer;
import net.turrem.utils.models.TVFFile;

public class RenderEngine
{
	private int index = 0;

	public RenderEngine()
	{
	}

	public RenderObject makeObject(TVFFile tvf, float scale, float x, float y, float z)
	{
		RenderObject obj = new RenderObject(this.index++);
		TVFBuffer buff = new TVFBuffer();
		buff.bindTVF(tvf, obj, scale, x, y, z);
		return obj;
	}
}

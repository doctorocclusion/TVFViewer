package net.turrem.viewer.render.object;

import net.turrem.viewer.render.IRenderer;

public interface IRenderObject extends IRenderer, IRenderObjectBase
{
	public int getEngineIndex();
}

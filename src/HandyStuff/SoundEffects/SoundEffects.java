package HandyStuff.SoundEffects;

import java.awt.Toolkit;

public class SoundEffects
{
	public static final void beep()
	{
		Toolkit.getDefaultToolkit().beep();

		final Runnable runnable = (Runnable) Toolkit.getDefaultToolkit().getDesktopProperty("win.sound.exclamation");
		if (runnable != null)
		{
			runnable.run();
		}
	}

	public static final void playSound_Success()
	{
		playSound(SoundEffectOptions.Asterisk);
	}

	public static final void playSound_Failure()
	{
		playSound(SoundEffectOptions.Exclamation);
	}

	public static final void playSound(SoundEffectOptions choice)
	{
		final Runnable runnable = (Runnable) Toolkit.getDefaultToolkit().getDesktopProperty(choice.getPropertyName());

		if (runnable != null)
		{
			runnable.run();
		}
	}

	public static enum SoundEffectOptions
	{
		/**
		 * the "something went wrong" sound
		 */
		Exclamation(
				"win.sound.exclamation"),
		DefaultBeep(
				"wind.sound.default"),
		Asterisk(
				"wind.sound.asterisk");

		private final String propertyName;

		private SoundEffectOptions(String inPropertyName)
		{
			this.propertyName = inPropertyName;
		}

		public final String getPropertyName()
		{
			return this.propertyName;
		}
	}
}

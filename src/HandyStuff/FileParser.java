package HandyStuff;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import CustomExceptions.DecheckedException;

public final class FileParser
{
	private static final String absPathToWorkingDirectory = System.getProperty("user.dir");
	
	public static final String getAbsPathToWorkingDirectory()
	{
		return absPathToWorkingDirectory;
	}
	
	public static final boolean createNewFile(String fullPath)
	{
		final File file = new File(fullPath);
		
		try
		{
			return file.createNewFile();
		}
		catch (IOException e)
		{
			throw new DecheckedException(e);
		}
	}

	public static final byte[] parseFileAsByteArray(String FileDirName)
	{
		return parseFileAsByteArray(Paths.get(FileDirName));
	}

	public static final byte[] parseFileAsByteArray(Path path)
	{
		byte[] RawBytes;

		try
		{
			RawBytes = Files.readAllBytes(path);
		}
		catch (IOException e)
		{
			throw new IllegalArgumentException("Illegal Path: " + path.toString());
		}

		return RawBytes;
	}

	public static final byte[] parseFileAsByteArray(File file)
	{
		final byte[] RawBytes;

		RawBytes = FileParser.parseFileAsByteArray(file.toPath());

		return RawBytes;
	}

	public static final String parseFileAsString(String FileDirName)
	{
		return new String(parseFileAsByteArray(FileDirName));
	}

	public static final String parseFileAsString(File inFile)
	{
		return new String(parseFileAsByteArray(inFile.toPath()));
	}

	public static final String[] parseFileAsStringLines(String FileDirName)
	{
		List<String> rawLines;

		try
		{
			rawLines = Files.readAllLines(Paths.get(FileDirName));
		}
		catch (IOException e)
		{
			e.printStackTrace();
			throw new IllegalArgumentException();
		}

		return rawLines.toArray(new String[rawLines.size()]);
	}

	public static final MappedByteBuffer parseFileAsMappedByteBuffer(String FileDirName)
	{
		final FileInputStream inputStream = helperMethod_MappedByteBuffer_getFileInputStream(FileDirName);
		final FileChannel channel = inputStream.getChannel();
		return helperMethod_MappedByteBuffer_getMappedByteBuffer(channel);
	}

	public static MappedByteBuffer parseFileAsMappedByteBuffer(Path inFullPath)
	{
		return helperMethod_MappedByteBuffer_getMappedByteBuffer(helperMethod_MappedByteBuffer_getFileInputStream(inFullPath).getChannel());
	}

	public static final MappedByteBuffer parseFileAsMappedByteBuffer(File inFile)
	{
		return helperMethod_MappedByteBuffer_getMappedByteBuffer(helperMethod_MappedByteBuffer_getFileInputStream(inFile).getChannel());
	}

	private static FileInputStream helperMethod_MappedByteBuffer_getFileInputStream(String dir)
	{
		try
		{
			return new FileInputStream(dir);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			throw new IllegalArgumentException();
		}
	}

	private static FileInputStream helperMethod_MappedByteBuffer_getFileInputStream(Path fullPath)
	{
		return helperMethod_MappedByteBuffer_getFileInputStream(fullPath.toFile());
	}

	private static FileInputStream helperMethod_MappedByteBuffer_getFileInputStream(File file)
	{
		try
		{
			return new FileInputStream(file);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			throw new IllegalArgumentException();
		}
	}

	private static MappedByteBuffer helperMethod_MappedByteBuffer_getMappedByteBuffer(FileChannel channel)
	{
		try
		{
			return channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public static final RandomAccessFile parseFileAsRandomAccessFile(String dirPath, String mode)
	{
		try
		{
			return new RandomAccessFile(dirPath, mode);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			System.err.println("\r\n\r\n");
			throw new IllegalArgumentException();
		}
	}

	public static final PrintWriter getPrintWriter(String inDir)
	{
		PrintWriter Printy = null;

		try
		{
			Printy = new PrintWriter(new BufferedWriter(new FileWriter(inDir)));
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return Printy;
	}

	public static final PrintWriter getPrintWriter(File inDir)
	{
		PrintWriter Printy = null;

		try
		{
			Printy = new PrintWriter(new BufferedWriter(new FileWriter(inDir)));
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return Printy;
	}

	public static final PrintStream getPrintStream(String inDir)
	{
		final PrintStream result;

		try
		{
			result = new PrintStream(inDir);
		}
		catch (FileNotFoundException e)
		{
			// e.printStackTrace();
			throw new IllegalArgumentException();
		}

		return result;
	}

	public static final PrintStream getPrintStream(File inFile)
	{
		final PrintStream result;

		try
		{
			result = new PrintStream(inFile);
		}
		catch (FileNotFoundException e)
		{
			// e.printStackTrace();
			throw new IllegalArgumentException();
		}

		return result;
	}

	public static final FileInputStream getFileInputStream(String fullPath)
	{
		try
		{
			return new FileInputStream(fullPath);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			throw new IllegalArgumentException();
		}
	}

	public static final FileInputStream getFileInputStream(Path fullPath)
	{
		return FileParser.getFileInputStream(fullPath.toFile());
	}

	public static final FileInputStream getFileInputStream(File file)
	{
		try
		{
			return new FileInputStream(file);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			throw new IllegalArgumentException();
		}
	}

	public static final FileOutputStream getFileOutputStream(String fullPath)
	{
		final File path = new File(fullPath);
		return getFileOutputStream(path);
	}
	
	public static final FileOutputStream getFileOutputStream(File path)
	{
		final File parent = path.getParentFile();
		parent.mkdirs();
		// parent.mkdir();

		try
		{
			return new FileOutputStream(path);
		}
		catch (FileNotFoundException e)
		{
			throw new DecheckedException(e);
		}
	}
	
	public static final void writeBytesToFile(String fullPath, byte[] cargo)
	{
		final FileOutputStream out = getFileOutputStream(fullPath);
		try
		{
			out.write(cargo);
			out.flush();
			out.close();
		}
		catch (IOException e)
		{
			throw new DecheckedException(e);
		}
	}

	public static final URL getUrl(String rawUrl)
	{
		try
		{
			return new URL(rawUrl);
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
			throw new IllegalArgumentException();
		}
	}

	public static final InputStream openStreamFromURL(URL in)
	{
		try
		{
			return in.openStream();
		}
		catch (IOException e)
		{
			final IllegalArgumentException temp = new IllegalArgumentException(in.toString());
			temp.initCause(e);
			throw temp;
		}
	}

	public static final InputStream openStreamFromURL(String rawUrl)
	{
		return FileParser.openStreamFromURL(FileParser.getUrl(rawUrl));
	}

	public static final void downloadFileFromUrlToPath(char[] rawUrl, char[] fileNameAndPath)
	{
		downloadFileFromUrlToPath(new String(rawUrl), new String(fileNameAndPath));
	}
	
	public static final void downloadFileFromUrlToPath(String rawUrl, String fileNameAndPath)
	{
		try
		{
			FileParser.downloadFileFromUrlToPath_throwing(rawUrl, fileNameAndPath);
		}
		catch (IOException e)
		{
			throw new IllegalArgumentException(e);
		}
	}

	private static final void downloadFileFromUrlToPath_throwing(String rawUrl, String fileNameAndPath) throws IOException
	{
		final InputStream is = FileParser.openStreamFromURL(rawUrl);
		final FileOutputStream os = FileParser.getFileOutputStream(fileNameAndPath);

		final byte[] b = new byte[2048];
		int length;

		while ((length = is.read(b)) != -1)
		{
			os.write(b, 0, length);
		}

		is.close();
		os.close();
	}
	
	public static final byte[] downloadFileFromUrl(String rawUrl)
	{
		final InputStream is = FileParser.openStreamFromURL(rawUrl);
		final ArrayList<ByteBundle> chunks = new ArrayList<ByteBundle>();
		int length = 0;
		
		loop: while(true)
		{
			final ByteBundle cur = ByteBundle.getInstance(is);
			
			if(cur.length != -1)
			{
				chunks.add(cur);
				length += cur.length;
			}
			else
			{
				break loop;
			}
		}
		
		final byte[] result = new byte[length];
		int offset = 0;
		
		for(int i = 0; i < chunks.size(); i++)
		{
			final ByteBundle cur = chunks.get(i);
			offset = cur.arrayCopy(result, offset);
		}
		
		return result;
	}
	
	private static class ByteBundle
	{
		private final int length;
		private final byte[] b;
		
		private ByteBundle(int inLength, byte[] inB)
		{
			super();
			this.length = inLength;
			this.b = inB;
		}
		
		public static final ByteBundle getInstance(InputStream is)
		{
			final byte[] b = new byte[2048];
			final int length;
			
			try
			{
				length = is.read(b);
			}
			catch (IOException e)
			{
				throw new DecheckedException(e);
			}
			
			return new ByteBundle(length, b);
		}
		
		public final int arrayCopy(byte[] result, int offset)
		{
			System.arraycopy(this.b, 0, result, offset, this.length);
			
			return this.length + offset;
		}
	}
	
	public static final BufferedImage parseFileAsBufferedImage(String filePath)
	{
		final File file = new File(filePath);
		
		try
		{
			return ImageIO.read(file);
		}
		catch (IOException e)
		{
			throw new DecheckedException(e);
		}
	}
	
	public static final BufferedImage parseStructuredBytesAsBufferedImage(byte[] in)
	{
		try
		{
			return ImageIO.read(new ByteArrayInputStream(in));
		}
		catch (IOException e)
		{
			throw new DecheckedException(e);
		}
	}
	
	public static final void displayImage(Image in)
	{
		displayImage(in, "");
	}
	
	public static final void displayImage(Image in, String windowTitle)
	{
		final int width = in.getWidth(null);
		final int height = in.getHeight(null);
		final MyCanvas canvas = new MyCanvas(in);
		final JFrame frame = new JFrame(windowTitle);
		frame.add(canvas);
		frame.setSize(width+50, height+50);
		frame.setVisible(true);
	}
	
	private static final class MyCanvas extends Canvas
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = -6117115901126562879L;
		
		private final Image image;
		
		MyCanvas(Image image)
		{
			this.image = image;
		}
		
		@Override
		public void paint(Graphics g)
		{
			g.drawImage(this.image, 0, 0, this);
		}
	}
}

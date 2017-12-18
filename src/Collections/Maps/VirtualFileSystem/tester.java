package Collections.Maps.VirtualFileSystem;

public class tester
{
	public static void main(String[] args)
	{
		// testToCharList();
		 testToCharList_withFiles();
		// testFileFetch();
		//testRelativeDir();
	}

	public static void testRelativeDir()
	{
		final Folder<String> test = bulkGenTestFolders();

		final String absPathRaw = "root/Lotus/Types/StoreItems/Consumables/Restoratives/TitaniaQuest";
		// absPath for relPath: root/Lotus/Types/Recipes/Weapons/ZhugeBlueprint
		final String relPathRaw = "Types/Recipes/Weapons/ZhugeBlueprint";

		final filePath absPath = filePathFactory.toAbsoluteFilePath(absPathRaw);
		final filePath relPath = filePathFactory.toRelativeFilePath(relPathRaw);

		final String target = test.getFile(absPath, relPath);

		System.out.println(target + "");
	}

	@SuppressWarnings("unused")
	private static void testFileFetch()
	{
		final Folder<String> test = generateFilledTestFolders();

		final String target = test.getFile(filePathFactory.toAbsoluteFilePath("root/test1/test2/testFile1".split("/")), true);

		System.out.println(target + "");
	}

	public static void testToCharList_withFiles()
	{
		final Folder<String> test = generateFilledTestFolders();

		System.out.println(test.toCharList(0, false, true).toString());
	}

	@SuppressWarnings("unused")
	private static void testToCharList()
	{
		final Folder<String> test = generateTestFolders();

		System.out.println(test.toCharList(0, false, true).toString());
	}

	private static Folder<String> generateFilledTestFolders()
	{
		final Folder<String> root = new Folder<String>("root");

		root.addFolder(filePathFactory.toAbsoluteFolderPath("root/test1/test2".split("/")), "testFolder1");
		root.addFolder(filePathFactory.toAbsoluteFolderPath("root/test1".split("/")), "testFolder2");
		root.addFolder(filePathFactory.toAbsoluteFolderPath("root/test3".split("/")), "testFolder4");

		root.addFile(filePathFactory.toAbsoluteFilePath("root/test1/test2/testFolder1/testFile1".split("/")), "testFile1Value");
		root.addFile(filePathFactory.toAbsoluteFilePath("root/test1/testFile2".split("/")), "testFile2Value");
		root.addFile(filePathFactory.toAbsoluteFilePath("root/test1/test2/testFile3".split("/")), "testFile3Value");
		root.addFile(filePathFactory.toAbsoluteFilePath("root/test1/testFolder2/testFile4".split("/")), "testFile4Value");

		return root;
	}

	private static <U> Folder<U> generateTestFolders()
	{
		final Folder<U> root = new Folder<U>("root");

		root.addFolder(filePathFactory.toAbsoluteFolderPath("root/test1/test2".split("/")), "testFolder1");
		root.addFolder(filePathFactory.toAbsoluteFolderPath("root/test1".split("/")), "testFolder2");
		root.addFolder(filePathFactory.toAbsoluteFolderPath("root/test1/test2".split("/")), "testFolder3");

		return root;
	}

	private static Folder<String> bulkGenTestFolders()
	{
		final String[] raw = new String[] {
											"root/Lotus/Types/Recipes/Weapons/TwinViperBlueprint",
											"root/Lotus/Types/Recipes/Weapons/U18ThrowingKnivesBlueprint",
											"root/Lotus/Types/Recipes/Weapons/VastoPrimeBlueprint",
											"root/Lotus/Types/Recipes/Weapons/VectisPrimeBlueprint",
											"root/Lotus/Types/Recipes/Weapons/VorsPistolBlueprint",
											"root/Lotus/Types/Recipes/Weapons/ZhugeBlueprint",
											"root/Lotus/Types/Recipes/CosmeticEnhancerBlueprint",
											"root/Lotus/Types/Recipes/CosmeticUnenhancerBlueprint",
											"root/Lotus/Types/Recipes/CronusBlueprint",
											"root/Lotus/Types/Recipes/DarkSwordBlueprint",
											"root/Lotus/Types/StoreItems/Consumables/Restoratives/TitaniaQuest/SpecterSummonFeyarchOberonBlueprint",
											"root/Lotus/Types/StoreItems/Consumables/Restoratives/TitaniaQuest/SpecterSummonKnaveLokiBlueprint",
											"root/Lotus/Types/StoreItems/Consumables/Restoratives/TitaniaQuest/SpecterSummonOrphidSarynBlueprint",
											"root/Lotus/Types/StoreItems/Consumables/Restoratives/Toxins/DayCommonAntitoxinBlueprint",
											"root/Lotus/Types/StoreItems/Consumables/Restoratives/Toxins/DayUnCommonAntitoxinBlueprint",
											"root/Lotus/Types/StoreItems/Consumables/Restoratives/Toxins/NightCommonAntitoxinBlueprint" };

		final Folder<String> root = new Folder<String>("root");

		for (int i = 0; i < raw.length; i++)
		{
			final filePath path = filePathFactory.toAbsoluteFilePath(raw[i]);
			final String value = "dummyFile" + i;

			root.addFile(path, value);
		}

		return root;
	}
}

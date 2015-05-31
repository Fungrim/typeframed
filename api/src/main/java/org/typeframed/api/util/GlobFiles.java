package org.typeframed.api.util;

import static java.nio.file.FileVisitResult.CONTINUE;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class GlobFiles {
	
	public static void main(String[] args) throws IOException {
		List<Path> f = find(Paths.get("/Users/larsan/Development/go/src/bitbucket.org/fungrim/prototest"), 
				"**/*.proto");
		for (Path p : f) {
			System.out.println(p);
		}
	}

	public static List<Path> find(Path root, String glob) throws IOException {
        Finder finder = new Finder(root, glob);
        Files.walkFileTree(root, finder);
		return finder.matches;
	}

	private static class Finder extends SimpleFileVisitor<Path> {

		private final PathMatcher matcher;
		private final List<Path> matches;
		private final Path root;

		Finder(Path root, String pattern) {
			this.root = root;
			matches = new ArrayList<Path>();
			matcher = FileSystems.getDefault().getPathMatcher("glob:" + pattern);
		}

		private void find(Path file) {
			Path relative = root.relativize(file);
			if (matcher.matches(relative)) {
				matches.add(file);
			}
		}

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
			find(file);
			return CONTINUE;
		}

		@Override
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
			return CONTINUE;
		}

		@Override
		public FileVisitResult visitFileFailed(Path file, IOException exc) {
			System.err.println(exc);
			return CONTINUE;
		}
	}
}

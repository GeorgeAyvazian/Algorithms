package algorithms;

import datastructures.graphs.Graph;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class FileWalking {

    private static String projectRootPath;

    public static void main(String[] args) throws IOException {
        projectRootPath = args[0];
        Path projectRoot = Paths.get(projectRootPath);
        Path ideaWorkspaceXMLPath = Paths.get(projectRootPath + "/.idea/workspace.xml");
        int breakpointStart, breakpointEnd;
        List<String> workspaceLines;
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(ideaWorkspaceXMLPath.toString())))) {
            workspaceLines = bufferedReader.lines().map(String::trim).collect(Collectors.toList());
            breakpointStart = workspaceLines.indexOf("<breakpoint-manager>") + 1;
            breakpointEnd = workspaceLines.indexOf("</breakpoint-manager>");
            for (int i = breakpointStart; i <= breakpointEnd; i++) {
                workspaceLines.remove(breakpointStart);
            }
        }
        workspaceLines.add(breakpointStart, "<breakpoints>");
        AtomicInteger atomicInteger = new AtomicInteger(breakpointStart);
        String pattern = Pattern.compile(".*" + args[1].toLowerCase() + ".*?\\{\\s*$").pattern();
        Pattern memberP = Pattern.compile("^\\s*?(private|protected)\\s*?([A-Z][^<|^>]+?)\\s+.*;.*");
        Map<String, List<String>> stringArrayListHashMap = new HashMap<>();
        Consumer<Path> memberOperation = path -> {
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(path.toString())))) {
                List<String> lines = bufferedReader.lines().collect(Collectors.toList());
                for (String line : lines) {
                    Matcher matcher = memberP.matcher(line);
                    if (matcher.matches()) {
                        String node = path.getFileName().toString().replace(".java", "");
                        List<String> strings1 = stringArrayListHashMap.get(node.toLowerCase());
                        String group = matcher.group(2);
                        if (strings1 == null) {
                            List<String> strings = new ArrayList<>();
                            strings.add(group.toLowerCase());
                            stringArrayListHashMap.put(node.toLowerCase(), strings);
                        } else {
                            if (!strings1.contains(group)) {
                                strings1.add(group.toLowerCase());
                            }
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        Consumer<Path> fileOperation = path -> {
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(path.toString())))) {
                List<String> lines = bufferedReader.lines().collect(Collectors.toList());
                lines.stream().filter(s -> s.toLowerCase().matches(pattern)).forEach(s -> {
                    String line1 = "<line-breakpoint enabled=\"true\" type=\"java-line\">" +
                            "<url>file://" + path.toString().replace(projectRootPath, "$PROJECT_DIR$") + "</url>" +
                            "<line>" + lines.indexOf(s) + "</line>" +
                            "<properties/>" +
                            "</line-breakpoint>";
                    if (workspaceLines.indexOf(line1) == -1) {
                        workspaceLines.add(atomicInteger.incrementAndGet(), line1);
                    }
                    String line2 = "<line-breakpoint enabled=\"true\" type=\"java-line\">" +
                            "<url>file://" + path.toString().replace(projectRootPath, "$PROJECT_DIR$") + "</url>" +
                            "<line>" + (lines.indexOf(s) + 1) + "</line>" +
                            "<properties/>" +
                            "</line-breakpoint>";
                    if (workspaceLines.indexOf(line2) == -1) {
                        workspaceLines.add(atomicInteger.incrementAndGet(), line2);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
//        JavaFileVisitor visitor = new JavaFileVisitor(fileOperation);
        JavaFileVisitor visitor = new JavaFileVisitor(memberOperation);
        Files.walkFileTree(projectRoot, visitor);
        Graph<String> stringGraph = new Graph<>(stringArrayListHashMap);
        System.out.println("==============>" + stringGraph.breadthFirstSearch("sku", "partner"));

        System.out.println(stringArrayListHashMap);
        workspaceLines.add(atomicInteger.incrementAndGet(), "</breakpoints>");
        workspaceLines.add(atomicInteger.incrementAndGet(), "<breakpoints-dialog>");
        workspaceLines.add(atomicInteger.incrementAndGet(), "<breakpoints-dialog />");
        workspaceLines.add(atomicInteger.incrementAndGet(), "</breakpoints-dialog>");
        workspaceLines.add(atomicInteger.incrementAndGet(), "<option name=\"time\" value=\"2\" />");
        workspaceLines.add(atomicInteger.incrementAndGet(), "</breakpoint-manager>");
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(ideaWorkspaceXMLPath.toString()))) {
            for (String str : workspaceLines) {
                bufferedWriter.write(str + '\n');
            }
            bufferedWriter.flush();
        }
        System.out.println(visitor.getJavaFilePaths());
    }

    private static class JavaFileVisitor implements FileVisitor<Path> {
        private final Function<Path, String> pathToStringMapper = Path::toString;
        private final Predicate<String> javaFileExtFilter = (String name) -> name.endsWith(".java");
        private final Collection<Path> javaFilePaths = new HashSet<>(100);
        private final Consumer<Path> fileOperation;

        private JavaFileVisitor(Consumer<Path> fileOperation) {this.fileOperation = fileOperation;}

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
            if (Optional.of(dir.toFile().getName()).filter(String::isEmpty).filter(name1 -> name1.startsWith(".")).isPresent()) {
                return FileVisitResult.SKIP_SUBTREE;
            }
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
            if (Optional.of(file).map(pathToStringMapper).filter(javaFileExtFilter).isPresent()) {
                javaFilePaths.add(file);
                fileOperation.accept(file);
            }
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) {
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
            return FileVisitResult.CONTINUE;
        }

        public Collection<Path> getJavaFilePaths() {
            return new HashSet<>(javaFilePaths);
        }
    }
}

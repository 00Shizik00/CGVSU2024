import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;
import com.cgvsu.math.Vector3f;
import com.cgvsu.objreader.ObjReader;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.File;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ObjWriterTest {

    public static void main(String[] args) throws IOException {
        Model model = new Model();

        model.vertices.add(new Vector3f(0, 0, 0));
        model.vertices.add(new Vector3f(1, 0, 0));
        model.vertices.add(new Vector3f(1, 1, 0));
        model.vertices.add(new Vector3f(0, 1, 0));

        Polygon triangle1 = new Polygon();
        triangle1.setVertexIndices(new ArrayList<>(List.of(0, 1, 2)));
        model.polygons.add(triangle1);

        Polygon triangle2 = new Polygon();
        triangle2.setVertexIndices(new ArrayList<>(List.of(0, 2, 3)));
        model.polygons.add(triangle2);

        Scanner scanner = new Scanner(System.in);

        // Шаг 1: Получаем путь для сохранения оригинального файла
        String filename = getValidFilePath(scanner, "Введите путь и имя файла для сохранения (например, test.obj):");

        // Сохраняем файл
        ObjWriter.write(model, filename);
        System.out.println("Объект успешно сохранен в: " + filename);

        // Шаг 2: Загрузка модели из файла
        String fileContent = Files.readString(Path.of(filename));
        Model loadedModel = ObjReader.read(fileContent);

        // Шаг 3: Получаем путь для сохранения переписанного файла
        String rewrittenFilename = getValidFilePath(scanner, "Введите путь и имя для переписанного файла (например, rewritten_test.obj):");

        // Сохраняем переписанный файл
        ObjWriter.write(loadedModel, rewrittenFilename);
        System.out.println("Переписанный объект успешно сохранен в: " + rewrittenFilename);

        // Шаг 4: Сравнение содержимого оригинального и переписанного файлов
        String originalContent = Files.readString(Path.of(filename));
        String rewrittenContent = Files.readString(Path.of(rewrittenFilename));

        // Сравниваем содержимое
        if (originalContent.equals(rewrittenContent)) {
            System.out.println("Файлы совпадают! Проверка прошла успешно.");
        } else {
            System.out.println("Ошибка! Содержимое файлов не совпадает.");
            System.out.println("Ожидаемое содержимое:\n" + originalContent);
            System.out.println("Содержимое переписанного файла:\n" + rewrittenContent);
        }
    }

    //Метод для проверки корректности сохранения файла
    private static String getValidFilePath(Scanner scanner, String prompt) {
        while (true) {
            System.out.println(prompt);
            String inputPath = scanner.nextLine();

            try {
                Path path = Path.of(inputPath);

                // Проверяем имя файла: наличие расширения и отсутствие недопустимых символов
                String fileName = path.getFileName() != null ? path.getFileName().toString() : "";
                if (!fileName.contains(".") || fileName.matches(".*[\\\\/:*?\"<>|].*")) {
                    System.out.println("Ошибка! Имя файла должно содержать расширение (например, .obj) и не содержать недопустимых символов.");
                    continue;
                }

                // Проверяем существование директории, если она указана
                Path parentDir = path.getParent();
                if (parentDir != null && !Files.exists(parentDir)) {
                    System.out.println("Ошибка! Директория для файла не существует.");
                    continue;
                }

                // Проверяем, что путь содержит разделитель, если директория не указана
                if (parentDir == null && !path.toString().contains("\\")) {
                    System.out.println("Ошибка! Укажите директорию для файла (например, D:\\folder\\test.obj).");
                    continue;
                }

                return inputPath; // Если все проверки пройдены, возвращаем путь
            } catch (InvalidPathException e) {
                System.out.println("Ошибка! Некорректный путь.");
            }
        }
    }
    @Test
    public void testFileCreation() throws IOException {

        Model model = new Model();
        model.vertices.add(new Vector3f(0, 0, 0));

        String testFilePath = "test.obj";

        ObjWriter.write(model, testFilePath);

        boolean fileCreated = new File(testFilePath).exists();

        assertTrue(fileCreated, "Файл не был создан!");

        if (fileCreated) {
            System.out.println("Файл успешно создан по пути: " + testFilePath);
        }
        new File(testFilePath).delete();
    }
}

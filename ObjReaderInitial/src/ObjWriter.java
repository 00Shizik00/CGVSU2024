
import com.cgvsu.model.Model;
import com.cgvsu.math.Vector2f;
import com.cgvsu.math.Vector3f;
import com.cgvsu.model.Polygon;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ObjWriter {

    public static void write(Model model, String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {

            for (Vector3f vertex : model.vertices) {
                writer.write("v " + vertex.x + " " + vertex.y + " " + vertex.z);
                writer.newLine();
            }

            for (Vector2f texture : model.textureVertices) {
                writer.write("vt " + texture.x + " " + texture.y);
                writer.newLine();
            }

            for (Vector3f normal : model.normals) {
                writer.write("vn " + normal.x + " " + normal.y + " " + normal.z);
                writer.newLine();
            }

            for (Polygon polygon : model.polygons) {
                writer.write("f");
                for (int i = 0; i < polygon.getVertexIndices().size(); i++) {
                    writer.write(" " + (polygon.getVertexIndices().get(i) + 1));

                    if (!polygon.getTextureVertexIndices().isEmpty()) {
                        writer.write("/" + (polygon.getTextureVertexIndices().get(i) + 1));
                    } else {
                        writer.write("/");
                    }

                    if (!polygon.getNormalIndices().isEmpty()) {
                        writer.write("/" + (polygon.getNormalIndices().get(i) + 1));
                    }
                }
                writer.newLine();
            }
        }
    }
}

package org.tomw.imagedao;

import java.io.File;

public interface ImageIdToFileConverter {
    File id2File(String id);

    String file2Id(File file);

    String file2Id(String fileName);
}

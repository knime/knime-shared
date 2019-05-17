package org.knime.core.util.workflowalizer2;

import java.io.File;

public interface ObjectStore {

    public String saveObject(File file, String name, String path, String workflowId);
}
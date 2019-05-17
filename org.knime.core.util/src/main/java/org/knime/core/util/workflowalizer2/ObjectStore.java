package org.knime.core.util.workflowalizer2;

public interface ObjectStore {

    public String saveObject(File file, String name, String path, String workflowId);
}
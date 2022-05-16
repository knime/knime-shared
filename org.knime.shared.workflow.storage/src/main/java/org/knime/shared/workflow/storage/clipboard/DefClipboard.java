/*
 * ------------------------------------------------------------------------
 *
 *  Copyright by KNIME AG, Zurich, Switzerland
 *  Website: http://www.knime.com; Email: contact@knime.com
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License, Version 3, as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 *  Additional permission under GNU GPL version 3 section 7:
 *
 *  KNIME interoperates with ECLIPSE solely via ECLIPSE's plug-in APIs.
 *  Hence, KNIME and ECLIPSE are both independent programs and are not
 *  derived from each other. Should, however, the interpretation of the
 *  GNU GPL Version 3 ("License") under any applicable laws result in
 *  KNIME and ECLIPSE being a combined program, KNIME AG herewith grants
 *  you the additional permission to use and propagate KNIME together with
 *  ECLIPSE with only the license terms in place for ECLIPSE applying to
 *  ECLIPSE and the GNU GPL Version 3 applying for KNIME, provided the
 *  license terms of ECLIPSE themselves allow for the respective use and
 *  propagation of ECLIPSE together with KNIME.
 *
 *  Additional permission relating to nodes for KNIME that extend the Node
 *  Extension (and in particular that are based on subclasses of NodeModel,
 *  NodeDialog, and NodeView) and that only interoperate with KNIME through
 *  standard APIs ("Nodes"):
 *  Nodes are deemed to be separate and independent programs and to not be
 *  covered works.  Notwithstanding anything to the contrary in the
 *  License, the License does not apply to Nodes, you are not required to
 *  license Nodes under the License, and you are granted a license to
 *  prepare and propagate Nodes, in each case even if such Nodes are
 *  propagated with or for interoperation with KNIME.  The owner of a Node
 *  may freely choose the license terms applicable to such Node, including
 *  when such Node is propagated with or for interoperation with KNIME.
 * ---------------------------------------------------------------------
 *
 * History
 *   19 May 2022 (carlwitt): created
 */
package org.knime.shared.workflow.storage.clipboard;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import org.knime.shared.workflow.storage.util.PasswordRedactor;

/**
 * A singleton storage for {@link DefClipboardContent} objects.
 *
 * A WorkflowManager can use this to store a complete version of copied contents that contains passwords. While handing
 * out to external users a redacted version with the passwords removed or encrypted. When a paste command is received,
 * the {@link DefClipboardContent#getPayloadIdentifier()} is compared to the payload identifier of the content stored
 * here. If the identifiers match, the WorkflowManager will use the content stored here, otherwise the given content.
 * This way we can still copy & paste nodes with passwords when we stay in the same JVM and also store copied content in
 * the system clipboard without exposing passwords.
 *
 * @see PasswordRedactor
 *
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 */
public final class DefClipboard {

    private static final DefClipboard INSTANCE = new DefClipboard();

    /**
     * @return the global storage for defs describing copy content to be pasted later
     */
    public static DefClipboard getInstance() {
        return INSTANCE;
    }

    AtomicReference<Optional<DefClipboardContent>> m_content = new AtomicReference<>(Optional.empty());

    /**
     * Set the content of the global def clipboard.
     *
     * @param content for later insertion.
     */
    public void setContent(final DefClipboardContent content) {
        m_content.set(Optional.ofNullable(content));
    }

    /**
     * @return the value last set with {@link #setContent(DefClipboardContent)}
     */
    public Optional<DefClipboardContent> getContent() {
        return m_content.get();
    }

    private DefClipboard() {
    }
}

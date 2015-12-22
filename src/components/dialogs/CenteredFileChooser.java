/**
 * Copyright 2012-2014 Naeregwen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package components.dialogs;

import java.awt.Component;
import java.awt.HeadlessException;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;

/**
 * @author Naeregwen
 *
 */
public class CenteredFileChooser extends JFileChooser {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 5724354835079983953L;
	
	private JComponent relative;

	public CenteredFileChooser(JComponent relative) {
		this.relative = relative;
	}
	
	/*/
	 * (non-Javadoc)
	 * @see javax.swing.JFileChooser#createDialog(java.awt.Component)
	 */
    protected JDialog createDialog(Component parent) throws HeadlessException {
        JDialog dialog = super.createDialog(parent);
        dialog.setLocationRelativeTo(relative);
        return dialog;
    }

}

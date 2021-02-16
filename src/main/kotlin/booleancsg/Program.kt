package booleancsg

import javax.swing.SwingUtilities
import javax.swing.UIManager

object Program {
    @JvmStatic
    fun main(args: Array<String>) {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
        SwingUtilities.invokeLater(::MainFrame)
    }
}

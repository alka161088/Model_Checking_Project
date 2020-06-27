package modelCheckCTL.util;

import java.util.*;
import java.io.*;
import java.nio.file.*;

public class Main extends form
{
    private KripkeStructure _kripke;
    public Main()
    {
        InitializeComponent();
    }

    private void buttonCheck_Click(Object sender, tangible.EventArgs e)
    {
        try
        {
            if (_kripke == null)
            {
                MessageBox.Show("Please load Kripke model", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return;
            }

            String originalExpression = this.textBoxCTLFormula.Text;
            String expression = originalExpression.replace(" ", "");
            String checkedStateID = this.comboBoxState.SelectedItem.toString();

            State checkedState = new State(checkedStateID);

            CtlFormula ctlFormula = new CtlFormula(expression, checkedState, this._kripke);
            boolean isSatisfy = ctlFormula.IsSatisfy();

            String message = GetMessage(isSatisfy, originalExpression, checkedStateID);
            this.textBoxCheckResult.Text = message;
        }
        catch (NumberFormatException fe)
        {
            MessageBox.Show(fe.getMessage(), "CTL syntax error", MessageBoxButtons.OK, MessageBoxIcon.Error);
        }
        catch (RuntimeException ex)
        {
            MessageBox.Show(ex.getMessage() + "\r\n" + ex.StackTrace, "Unexpected error", MessageBoxButtons.OK, MessageBoxIcon.Error);
        }
    }

    public final String GetMessage(boolean isSatisfy, String expression, String stateID)
    {
        String message = String.format("Property %1$s %2$s in state %3$s", expression, isSatisfy ? "holds" : "does not hold", stateID);

        return message;
    }

    private void loadModelToolStripMenuItem_Click(Object sender, tangible.EventArgs e)
    {
        //open file
        OpenFileDialog openFileDialog = new OpenFileDialog();
        openFileDialog.CheckFileExists = true;
        openFileDialog.CheckPathExists = true;
        openFileDialog.DefaultExt = ".txt";
        openFileDialog.InitialDirectory = Environment.GetFolderPath(Environment.SpecialFolder.Desktop);

        if (DialogResult.OK != openFileDialog.ShowDialog())
        {
            return;
        }

        Reset();

        try
        {
            String inputFileName = openFileDialog.FileName;
            String kripkeString = Files.readString(inputFileName);

            KripkeStructure kripke = new KripkeStructure(kripkeString);
            _kripke = kripke;

            //display loaded kripke
            this.groupBoxModel.Text = (new File(openFileDialog.FileName)).getName();
            this.textBoxModelDescription.Text = _kripke.toString();

            //fill state combo box
            for (State state : _kripke.States)
            {
                comboBoxState.Items.Add(state.StateName);
            }
            comboBoxState.SelectedIndex = 0;
        }
        catch (NumberFormatException fe)
        {
            MessageBox.Show(fe.getMessage(), "CTL syntax error", MessageBoxButtons.OK, MessageBoxIcon.Error);
        }
        catch (RuntimeException ex)
        {
            MessageBox.Show(ex.getMessage() + "\r\n" + ex.StackTrace, "Unexpected error", MessageBoxButtons.OK, MessageBoxIcon.Error);
        }
    }

    private void Reset()
    {
        this.comboBoxState.Items.Clear();
        this.textBoxCTLFormula.Text = "";
        this.textBoxCheckResult.Text = "";
        this.groupBoxModel.Text = "Model";
        this.textBoxModelDescription.Text = "";
        this._kripke = null;
    }

    private void exitToolStripMenuItem_Click(Object sender, tangible.EventArgs e)
    {
        this.Close();
    }
}


package tangible;

class EventArgs
{
    public static EventArgs Empty;

    public EventArgs()
    {
    }
}


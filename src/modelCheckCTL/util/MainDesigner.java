package modelcheckctl;

public class Main
{
    /**
     Required designer variable.
     */
    private System.ComponentModel.IContainer components = null;

    /**
     Clean up any resources being used.

     @param disposing true if managed resources should be disposed; otherwise, false.
     */
    @Override
    protected void Dispose(boolean disposing)
    {
        if (disposing && (components != null))
        {
            components.Dispose();
        }
        super.Dispose(disposing);
    }

    ///#region Windows Form Designer generated code

    /**
     Required method for Designer support - do not modify
     the contents of this method with the code editor.
     */
    private void InitializeComponent()
    {
        System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(Main.class);
        this.menuStrip1 = new System.Windows.Forms.MenuStrip();
        this.fileToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
        this.loadModelToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
        this.exitToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
        this.comboBoxState = new System.Windows.Forms.ComboBox();
        this.label3 = new System.Windows.Forms.Label();
        this.textBoxCTLFormula = new System.Windows.Forms.TextBox();
        this.label4 = new System.Windows.Forms.Label();
        this.textBoxModelDescription = new System.Windows.Forms.TextBox();
        this.buttonCheck = new System.Windows.Forms.Button();
        this.textBoxCheckResult = new System.Windows.Forms.TextBox();
        this.label1 = new System.Windows.Forms.Label();
        this.groupBoxModel = new System.Windows.Forms.GroupBox();
        this.menuStrip1.SuspendLayout();
        this.groupBoxModel.SuspendLayout();
        this.SuspendLayout();
        //
        // menuStrip1
        //
        this.menuStrip1.Items.AddRange(new System.Windows.Forms.ToolStripItem[] {this.fileToolStripMenuItem});
        this.menuStrip1.Location = new System.Drawing.Point(0, 0);
        this.menuStrip1.Name = "menuStrip1";
        this.menuStrip1.Size = new System.Drawing.Size(517, 24);
        this.menuStrip1.TabIndex = 6;
        this.menuStrip1.Text = "menuStrip1";
        //
        // fileToolStripMenuItem
        //
        this.fileToolStripMenuItem.DropDownItems.AddRange(new System.Windows.Forms.ToolStripItem[] {this.loadModelToolStripMenuItem, this.exitToolStripMenuItem});
        this.fileToolStripMenuItem.Name = "fileToolStripMenuItem";
        this.fileToolStripMenuItem.Size = new System.Drawing.Size(37, 20);
        this.fileToolStripMenuItem.Text = "File";
        //
        // loadModelToolStripMenuItem
        //
        this.loadModelToolStripMenuItem.Name = "loadModelToolStripMenuItem";
        this.loadModelToolStripMenuItem.Size = new System.Drawing.Size(137, 22);
        this.loadModelToolStripMenuItem.Text = "Load Model";

        this.loadModelToolStripMenuItem.Click += new System.EventHandler(this.loadModelToolStripMenuItem_Click);
        //
        // exitToolStripMenuItem
        //
        this.exitToolStripMenuItem.Name = "exitToolStripMenuItem";
        this.exitToolStripMenuItem.Size = new System.Drawing.Size(137, 22);
        this.exitToolStripMenuItem.Text = "Exit";

        this.exitToolStripMenuItem.Click += new System.EventHandler(this.exitToolStripMenuItem_Click);
        //
        // comboBoxState
        //
        this.comboBoxState.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
        this.comboBoxState.FormattingEnabled = true;
        this.comboBoxState.Location = new System.Drawing.Point(85, 44);
        this.comboBoxState.Name = "comboBoxState";
        this.comboBoxState.Size = new System.Drawing.Size(121, 21);
        this.comboBoxState.TabIndex = 7;
        //
        // label3
        //
        this.label3.AutoSize = true;
        this.label3.Location = new System.Drawing.Point(12, 47);
        this.label3.Name = "label3";
        this.label3.Size = new System.Drawing.Size(32, 13);
        this.label3.TabIndex = 8;
        this.label3.Text = "State";
        //
        // textBoxCTLFormula
        //
        this.textBoxCTLFormula.Location = new System.Drawing.Point(85, 75);
        this.textBoxCTLFormula.Name = "textBoxCTLFormula";
        this.textBoxCTLFormula.Size = new System.Drawing.Size(121, 20);
        this.textBoxCTLFormula.TabIndex = 9;
        //
        // label4
        //
        this.label4.AutoSize = true;
        this.label4.Location = new System.Drawing.Point(12, 78);
        this.label4.Name = "label4";
        this.label4.Size = new System.Drawing.Size(67, 13);
        this.label4.TabIndex = 10;
        this.label4.Text = "CTL Formula";
        //
        // textBoxModelDescription
        //
        this.textBoxModelDescription.BackColor = System.Drawing.SystemColors.Window;

        //ORIGINAL LINE: this.textBoxModelDescription.Font = new System.Drawing.Font("Arial", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
        this.textBoxModelDescription.Font = new System.Drawing.Font("Arial", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
        this.textBoxModelDescription.Location = new System.Drawing.Point(13, 20);
        this.textBoxModelDescription.Multiline = true;
        this.textBoxModelDescription.Name = "textBoxModelDescription";
        this.textBoxModelDescription.ReadOnly = true;
        this.textBoxModelDescription.ScrollBars = System.Windows.Forms.ScrollBars.Vertical;
        this.textBoxModelDescription.Size = new System.Drawing.Size(251, 196);
        this.textBoxModelDescription.TabIndex = 11;
        //
        // buttonCheck
        //
        this.buttonCheck.Location = new System.Drawing.Point(131, 101);
        this.buttonCheck.Name = "buttonCheck";
        this.buttonCheck.Size = new System.Drawing.Size(75, 23);
        this.buttonCheck.TabIndex = 12;
        this.buttonCheck.Text = "Check";
        this.buttonCheck.UseVisualStyleBackColor = true;
        this.buttonCheck.Click += new System.EventHandler(this.buttonCheck_Click);
        //
        // textBoxCheckResult
        //
        this.textBoxCheckResult.BackColor = System.Drawing.SystemColors.Window;
        this.textBoxCheckResult.Location = new System.Drawing.Point(12, 158);
        this.textBoxCheckResult.Multiline = true;
        this.textBoxCheckResult.Name = "textBoxCheckResult";
        this.textBoxCheckResult.ReadOnly = true;
        this.textBoxCheckResult.Size = new System.Drawing.Size(194, 85);
        this.textBoxCheckResult.TabIndex = 13;
        //
        // label1
        //
        this.label1.AutoSize = true;
        this.label1.Location = new System.Drawing.Point(12, 142);
        this.label1.Name = "label1";
        this.label1.Size = new System.Drawing.Size(37, 13);
        this.label1.TabIndex = 14;
        this.label1.Text = "Result";
        //
        // groupBoxModel
        //
        this.groupBoxModel.Controls.Add(this.textBoxModelDescription);
        this.groupBoxModel.Location = new System.Drawing.Point(230, 27);
        this.groupBoxModel.Name = "groupBoxModel";
        this.groupBoxModel.Size = new System.Drawing.Size(275, 225);
        this.groupBoxModel.TabIndex = 16;
        this.groupBoxModel.TabStop = false;
        this.groupBoxModel.Text = "Model";
        //
        // Main
        //
        this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
        this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
        this.ClientSize = new System.Drawing.Size(517, 267);
        this.Controls.Add(this.groupBoxModel);
        this.Controls.Add(this.label1);
        this.Controls.Add(this.textBoxCheckResult);
        this.Controls.Add(this.buttonCheck);
        this.Controls.Add(this.label4);
        this.Controls.Add(this.textBoxCTLFormula);
        this.Controls.Add(this.label3);
        this.Controls.Add(this.comboBoxState);
        this.Controls.Add(this.menuStrip1);
        this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedSingle;
        this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
        this.MainMenuStrip = this.menuStrip1;
        this.MaximizeBox = false;
        this.Name = "Main";
        this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
        this.Text = "CTL Model Checker";
        this.menuStrip1.ResumeLayout(false);
        this.menuStrip1.PerformLayout();
        this.groupBoxModel.ResumeLayout(false);
        this.groupBoxModel.PerformLayout();
        this.ResumeLayout(false);
        this.PerformLayout();

    }

   ///#endregion

    private System.Windows.Forms.MenuStrip menuStrip1;
    private System.Windows.Forms.ToolStripMenuItem fileToolStripMenuItem;
    private System.Windows.Forms.ToolStripMenuItem loadModelToolStripMenuItem;
    private System.Windows.Forms.ToolStripMenuItem exitToolStripMenuItem;
    private System.Windows.Forms.ComboBox comboBoxState;
    private System.Windows.Forms.Label label3;
    private System.Windows.Forms.TextBox textBoxCTLFormula;
    private System.Windows.Forms.Label label4;
    private System.Windows.Forms.TextBox textBoxModelDescription;
    private System.Windows.Forms.Button buttonCheck;
    private System.Windows.Forms.TextBox textBoxCheckResult;
    private System.Windows.Forms.Label label1;
    private System.Windows.Forms.GroupBox groupBoxModel;
}

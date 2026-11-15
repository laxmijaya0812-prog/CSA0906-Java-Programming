import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * The ORIGINAL faulty program from the assignment (class renamed to DrawPadBuggy so that it
 * can sit beside the repaired DrawPad). Only the imports were added so that it compiles.
 * Use it to reproduce Task 4.1 / 4.2 / 4.4.
 */
public class DrawPadBuggy extends Frame {
    int count = 0; Label status = new Label("Clicks: 0");
    public DrawPadBuggy() {
        add(status, BorderLayout.SOUTH);
        addMouseListener(new MouseAdapter() {
            public void mouseclicked(MouseEvent e) {          // <-- DEFECT: lower-case 'c', no @Override
                count++;
                Graphics g = getGraphics(); g.fillOval(e.getX(), e.getY(), 6, 6);
            }
        });
        setSize(400, 300); setVisible(true);
    }
    public static void main(String[] args) { new DrawPadBuggy(); }
}

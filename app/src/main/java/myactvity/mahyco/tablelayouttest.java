package myactvity.mahyco;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;


public class tablelayouttest extends AppCompatActivity {



        /** Called when the activity is first created. */
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_tablelayouttest);

            TableRow.LayoutParams wrapWrapTableRowParams = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            int[] fixedColumnWidths = new int[]{20, 20, 20, 20, 20};
            int[] scrollableColumnWidths = new int[]{20, 20, 20, 30, 30};
            int fixedRowHeight = 50;
            int fixedHeaderHeight = 60;

            TableRow row = new TableRow(this);
            //header (fixed vertically)
            TableLayout header = (TableLayout) findViewById(R.id.table_header);
            row.setLayoutParams(wrapWrapTableRowParams);
            row.setGravity(Gravity.CENTER);
            row.setBackgroundColor(Color.YELLOW);
            row.addView(makeTableRowWithText("col 1", fixedColumnWidths[0], fixedHeaderHeight));
            row.addView(makeTableRowWithText("col 2", fixedColumnWidths[1], fixedHeaderHeight));
            row.addView(makeTableRowWithText("col 3", fixedColumnWidths[2], fixedHeaderHeight));
            row.addView(makeTableRowWithText("col 4", fixedColumnWidths[3], fixedHeaderHeight));
            row.addView(makeTableRowWithText("col 5", fixedColumnWidths[4], fixedHeaderHeight));
            header.addView(row);
            //header (fixed horizontally)
            TableLayout fixedColumn = (TableLayout) findViewById(R.id.fixed_column);
            //rest of the table (within a scroll view)
            TableLayout scrollablePart = (TableLayout) findViewById(R.id.scrollable_part);
            for(int i = 0; i < 100; i++) {
                TextView fixedView = makeTableRowWithText("row number " + i, scrollableColumnWidths[0], fixedRowHeight);
                fixedView.setBackgroundColor(Color.BLUE);
                fixedColumn.addView(fixedView);
                row = new TableRow(this);
                row.setLayoutParams(wrapWrapTableRowParams);
                row.setGravity(Gravity.CENTER);
                row.setBackgroundColor(Color.WHITE);
                row.addView(makeTableRowWithText("value 2", scrollableColumnWidths[1], fixedRowHeight));
                row.addView(makeTableRowWithText("value 3", scrollableColumnWidths[2], fixedRowHeight));
                row.addView(makeTableRowWithText("value 4", scrollableColumnWidths[3], fixedRowHeight));
                row.addView(makeTableRowWithText("value 5", scrollableColumnWidths[4], fixedRowHeight));
                scrollablePart.addView(row);
            }

        }


        //util method
        private TextView recyclableTextView;

    public TextView makeTableRowWithText(String text, int widthInPercentOfScreenWidth, int fixedHeightInPixels) {
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        recyclableTextView = new TextView(this);
        recyclableTextView.setText(text);
        recyclableTextView.setTextColor(Color.BLACK);
        recyclableTextView.setTextSize(20);
        recyclableTextView.setWidth(widthInPercentOfScreenWidth * screenWidth / 100);
        recyclableTextView.setHeight(fixedHeightInPixels);
        return recyclableTextView;
    }

}

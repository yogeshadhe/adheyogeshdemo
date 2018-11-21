package com.banjarasathi;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

class ExpandableListAdapter extends BaseExpandableListAdapter
{
    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
    String sales_attendance_person1;

    public ExpandableListAdapter(Context context, List<String> listDataHeader, HashMap<String, List<String>> listChildData)
    {
        Log.i("ExpandableListAdapter00","ExpandableListAdapter1");
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        // sales_attendance_person1= BaseActivity.sales_attendance_person;
    }
    @Override
    public Object getChild(int groupPosition, int childPosititon)
    {
        Log.i("ExpandableListAdapter1","ExpandableListAdapter1");
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosititon);
    }
    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        Log.i("ExpandableListAdapter2","ExpandableListAdapter1");
        return childPosition;
    }
    @Override
    public View getChildView(int groupPosition,

                             final int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {
        Log.i("ExpandableListAdapter_c","ExpandableListAdapter1");
        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null)
        {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.navdrawer_child_item, null);
        }
        Log.i("groupPosition",""+groupPosition);
        Log.i("childPosition",""+childPosition);

        TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);
        txtListChild.setText(childText);
        ImageView image_child = (ImageView) convertView.findViewById(R.id.img_child_item);


            /*if (groupPosition==2)
            {
                image_child.setImageResource(BaseActivity.icon_child_event[childPosition]);
            }*/
            /*if (groupPosition==5)
            {
                image_child.setImageResource(BaseActivity.icon_child_leave[childPosition]);
            }
            if (groupPosition==6)
            {
                image_child.setImageResource(BaseActivity.icon_child[childPosition]);

            }
            if (groupPosition==7)
            {
                image_child.setImageResource(BaseActivity.icon_child1[childPosition]);

            }

*/
        return convertView;
        //return  null;
    }
    @Override
    public int getChildrenCount(int groupPosition)
    {
        Log.i("ExpandableListAdapter4","ExpandableListAdapter1");
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
    }
    @Override
    public Object getGroup(int groupPosition)
    {
        Log.i("ExpandableListAdapter5","ExpandableListAdapter1");
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount()
    {
        Log.i("ExpandableListAdapter6","ExpandableListAdapter1");
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition)
    {
        Log.i("ExpandableListAdapter7","ExpandableListAdapter1");
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {  Log.i("ExpandableListAdapter_g","ExpandableListAdapter1");
        String headerTitle = (String) getGroup(groupPosition);
        Log.i("headerTitle","headerTitle"+headerTitle);

        if (convertView == null)
        {
            Log.i("convertView","convertView"+convertView);
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.navdrawer_group_item, null);
        }
        TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
        lblListHeader.setText(headerTitle);

        // adding icon to expandable list view
        ImageView imgListGroup = (ImageView) convertView.findViewById(R.id.ic_txt);
        //ImageView imgListGroup_arrow = (ImageView) convertView.findViewById(R.id.group_arrow);
        ImageView imgListGroup_arrow = (ImageView) convertView.findViewById(R.id.group_arrow);



            imgListGroup.setImageResource(BaseActivity.icon[groupPosition]);


           Log.i("convertView", "convertView" + convertView);
            Log.i("groupPosition", "groupPosition" + groupPosition);

           /* if (groupPosition == 2) {
                imgListGroup_arrow.setImageResource(R.drawable.plus_icon);
            }
            if (groupPosition == 5) {
                imgListGroup_arrow.setImageResource(R.drawable.plus_icon);
            }
            if (groupPosition == 6) {
                imgListGroup_arrow.setImageResource(R.drawable.plus_icon);
            }
            if (groupPosition == 7) {
                imgListGroup_arrow.setImageResource(R.drawable.plus_icon);
            }
            if (groupPosition == 2 || groupPosition == 5 || groupPosition == 6 || groupPosition == 7) {
                if (isExpanded) {
                    imgListGroup_arrow.setImageResource(R.drawable.minus_icon);
                }
            }

            if (groupPosition == 3) {

                imgListGroup_arrow.setImageDrawable(null);

            }
            if (groupPosition == 4) {

                imgListGroup_arrow.setImageDrawable(null);

            }
*/
        return convertView;
    }

    @Override
    public boolean hasStableIds()
    {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


}

package org.styleru.hseday.NavigationFragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import org.styleru.hseday.FacultiesListElement;
import org.styleru.hseday.MainActivity;
import org.styleru.hseday.RecyclerViewAdapters.RecyclerViewAdapterOrganisations;

import java.util.List;


public class FragmentOrganisations extends android.support.v4.app.Fragment {
    private List<FacultiesListElement> faculties;
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapterOrganisations mAdapter;
    private StaggeredGridLayoutManager mGridLayoutManager;
    private String[] mList;

    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FragmentOrganisations() {
    }

    // TODO: Rename and change types and number of parameters
    public static FragmentOrganisations newInstance(String param1, String param2) {
        FragmentOrganisations fragment = new FragmentOrganisations();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((MainActivity) getActivity()).setActionBarTitle("Организации");

        View view = inflater.inflate(org.styleru.hseday.R.layout.fragment_organisations, container, false);

        mList = getResources().getStringArray(org.styleru.hseday.R.array.organisations);
        mAdapter = new RecyclerViewAdapterOrganisations(getActivity(), mList);
        mRecyclerView = (RecyclerView)view.findViewById(org.styleru.hseday.R.id.organisations_recycler_view);
        mGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mAdapter.setHasStableIds(true);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(org.styleru.hseday.R.id.map_quest_mark).setVisible(false);
        menu.findItem(org.styleru.hseday.R.id.map_ball).setVisible(false);
        menu.findItem(org.styleru.hseday.R.id.map_tent).setVisible(false);
        menu.findItem(org.styleru.hseday.R.id.map_paper).setVisible(false);
        menu.findItem(org.styleru.hseday.R.id.map_microphone).setVisible(false);
    }
}
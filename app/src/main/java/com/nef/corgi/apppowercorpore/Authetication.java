package com.nef.corgi.apppowercorpore;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

import com.nef.corgi.apppowercorpore.DTO.UserDTO;

import es.dmoral.toasty.Toasty;


public class Authetication extends Fragment {
    private UserDTO user;
    private OnFragmentInteractionListener mListener;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
   public Context context;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public Authetication(){}
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(UserDTO userDTO);
    }
    public static Authetication newInstance(String param1, String param2) {
        Authetication fragment = new Authetication();
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
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_fragment_auth, container, false);
        final EditText name = fragment.findViewById(R.id.fragment_auth_edit_name);
        final EditText pass = fragment.findViewById(R.id.fragment_auth_edit_pass);
        final EditText email = fragment.findViewById(R.id.fragment_auth_edit_email);
        Button connect = fragment.findViewById(R.id.fragment_auth_button);
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String s_user = name.getEditableText().toString();
               String s_pass = pass.getEditableText().toString();
                String s_email = email.getEditableText().toString();
                try {
                    user = new UserDTO(s_user, s_pass, s_email);
                    mListener.onFragmentInteraction(user);//le pasamos el usuario
                }catch (UserDTO.MalformedUserException e){
                    //TODO revisar esto xq da fallo por los valores minimos de las excepciones
                   Toasty.error(getActivity(),getString(R.string.Incorrect_Values),Toast.LENGTH_SHORT).show();
                    user=null;
                }


            }

        });
        return fragment;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
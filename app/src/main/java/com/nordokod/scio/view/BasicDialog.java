package com.nordokod.scio.view;

interface BasicDialog {

    void initDialog();

    void initComponents();

    void initListeners();

    void showDialog();

    void changeStarState(int number_of_stars);
}

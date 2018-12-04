package cl.telematica.basicconnectionexample.main;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.List;

import cl.telematica.basicconnectionexample.R;
import cl.telematica.basicconnectionexample.connection.RetrofitClientInstance;
import cl.telematica.basicconnectionexample.main.presenter.MainPresenterImpl;
import cl.telematica.basicconnectionexample.models.Libro;
import cl.telematica.basicconnectionexample.main.view.MainView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MainView {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) { //Bundle sirve para guardar datos y pasarlo a las Activities/Fragments
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recyclerView);

        setRecyclerViewParams();

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading . . . . .");
        progressDialog.show();

        //MainPresenterImpl presenter = new MainPresenterImpl(this);
        //presenter.fetchData("http://www.mocky.io/v2/5bfc6aa9310000780039be36", 15000);

        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<List<Libro>> call = service.getAllData();
        call.enqueue(new Callback<List<Libro>>() {
            @Override
            public void onResponse(Call<List<Libro>> call, Response<List<Libro>> response) {
                progressDialog.dismiss();
                populateRecyclerView(response.body());
            }

            @Override
            public void onFailure(Call<List<Libro>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "AYURAAAAAA", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void setRecyclerViewParams() {
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    public void populateRecyclerView(List<Libro> libros) {
        mAdapter = new UIAdapter(libros, this);
        mRecyclerView.setAdapter(mAdapter);
    }
}

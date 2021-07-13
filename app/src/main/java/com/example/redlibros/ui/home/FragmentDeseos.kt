package com.example.redlibros.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.redlibros.DataBase.QueryFirestore
import com.example.redlibros.R
import com.example.redlibros.Recycler.Item
import com.example.redlibros.Recycler.ItemAdapter
import com.example.redlibros.databinding.FragmentHomeBinding
import com.google.firebase.firestore.FirebaseFirestore

class FragmentDeseos : Fragment() , ItemAdapter.ItemClickListener{
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    val db = FirebaseFirestore.getInstance()

    private lateinit  var recycler : RecyclerView
    private lateinit  var lista : MutableList<Item>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        recycler = binding.mRecyclerView
        recycler.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        val marginLayoutParams = binding.mRecyclerView.layoutParams as ViewGroup.MarginLayoutParams
        marginLayoutParams.topMargin = 0
        binding.mRecyclerView.layoutParams = marginLayoutParams
        llenarLista()




        return root
    }

    private fun llenarLista() {
        lista = mutableListOf()
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val emailPref = prefs.getString("email","")
        var book =QueryFirestore().booksforUser(emailPref.toString(), "userDeseo")
        book.addOnSuccessListener { DobumentBook->

            for (elemento in DobumentBook ) {
                lista.add(
                      Item(elemento.data?.get("id").toString(), elemento.data?.get("title").toString(), elemento.data?.get("authors").toString(), elemento.data?.get("description").toString(), elemento.data?.get("image").toString() )
                )

            }
            val adapter = ItemAdapter(this.lista, this)
            recycler.adapter = adapter
        }

        /*lista = listOf(
            Item("Flowers For Algernon", "Daniel Keyes", "The classic novel about a daring experiment in human intelligence Charlie Gordon, IQ 68, is a floor sweeper and the gentle butt of everyone's jokes - until an experiment in the enhancement of human intelligence turns him into a genius. But then Algernon, the mouse whose triumphal experimental transformation preceded his, fades and dies, and Charlie has to face the possibility that his salvation was only temporary.","https://books.google.com/books/content?id=8Pr_kLFxciYC&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api"),
            Item("Sombra Y Hueso", "Leigh Bardugo","Alina is an expendable soldier, but when her unit is attacked, she discovers magic she did not know she had. When she trains with the Grisha, she is bewitched by their leader, the Darkling. He believes her powers can destroy the Shadow Fold and reunite the country. A secret from her past could destroy the nation and everything she loves.","https://books.google.com/books/content?id=oWWNzQEACAAJ&printsec=frontcover&img=1&zoom=1&source=gbs_api"),
            Item("A Dance with Dragons", "G.R.R Martin","#1 NEW YORK TIMES BESTSELLER • THE BOOK BEHIND THE FIFTH SEASON OF THE ACCLAIMED HBO SERIES GAME OF THRONES Don’t miss the thrilling sneak peek of George R. R. Martin’s A Song of Ice and Fire: Book Six, The Winds of Winter Dubbed “the American Tolkien” by Time magazine, George R. R. Martin has earned international acclaim for his monumental cycle of epic fantasy. Now the #1 New York Times bestselling author delivers the fifth book in his landmark series—as both familiar faces and surprising new forces vie for a foothold in a fragmented empire. A DANCE WITH DRAGONS A SONG OF ICE AND FIRE: BOOK FIVE In the aftermath of a colossal battle, the future of the Seven Kingdoms hangs in the balance—beset by newly emerging threats from every direction. In the east, Daenerys Targaryen, the last scion of House Targaryen, rules with her three dragons as queen of a city built on dust and death. But Daenerys has thousands of enemies, and many have set out to find her. As they gather, one young man embarks upon his own quest for the queen, with an entirely different goal in mind. Fleeing from Westeros with a price on his head, Tyrion Lannister, too, is making his way to Daenerys. But his newest allies in this quest are not the rag-tag band they seem, and at their heart lies one who could undo Daenerys’s claim to Westeros forever. Meanwhile, to the north lies the mammoth Wall of ice and stone—a structure only as strong as those guarding it. There, Jon Snow, 998th Lord Commander of the Night’s Watch, will face his greatest challenge. For he has powerful foes not only within the Watch but also beyond, in the land of the creatures of ice. From all corners, bitter conflicts reignite, intimate betrayals are perpetrated, and a grand cast of outlaws and priests, soldiers and skinchangers, nobles and slaves, will face seemingly insurmountable obstacles. Some will fail, others will grow in the strength of darkness. But in a time of rising restlessness, the tides of destiny and politics will lead inevitably to the greatest dance of all. Praise for A Dance with Dragons “Filled with vividly rendered set pieces, unexpected turnings, assorted cliffhangers and moments of appalling cruelty, A Dance with Dragons is epic fantasy as it should be written: passionate, compelling, convincingly detailed and thoroughly imagined.”—The Washington Post “Long live George Martin . . . a literary dervish, enthralled by complicated characters and vivid language, and bursting with the wild vision of the very best tale tellers.”—The New York Times “One of the best series in the history of fantasy.”—Los Angeles Times","https://books.google.com/books/content?id=7Q4R3RHe8AQC&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api"),
            Item("H. P. Lovecraft", "H. P. Lovecraft" ,"This luxury box set contains all three of H. P. Lovecraft's most popular books: Los mitos de cthulhu, En las montañas de la locura, and El horror de dunwich y otros relatos. In addition to these three titles, the package also includes an exclusively designed notebook to go with the books. It is a wonderful gift for lovers of great literature in general and especially so for admirers of the great master of horror.","https://books.google.com/books/content?id=n4tEzQEACAAJ&printsec=frontcover&img=1&zoom=1&source=gbs_api"),
            Item("City of Bones", "Cassandra Clare", "16-year-old Clary Fray is an ordinary teenager, who likes hanging out in Brooklyn with her friends. But everything changes the night she witnesses a murder, committed by a group of teens armed with medieval weaponry.","https://books.google.com/books/content?id=Xhq3NAEACAAJ&printsec=frontcover&img=1&zoom=1&source=gbs_api"),
        )*/
    }

    override fun onItemClick(element: Item) {
        val arg = Bundle().apply {
            putString("id", element.id)
            putString("name",element.name)
            putString("author",element.author)
            putString("des",element.description)
            putString("url",element.url)
        }

        findNavController().navigate(R.id.action_nav_LibrosDeseos_to_detail_fragment2,arg)

    }


}
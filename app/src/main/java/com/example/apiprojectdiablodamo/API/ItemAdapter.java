package com.example.apiprojectdiablodamo.API;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.apiprojectdiablodamo.R;
import com.example.apiprojectdiablodamo.ui.PreferitsListManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private List<Item> listaItems;
    private final Context context;
    private FirebaseFirestore mFirestore;
    private List<Item> listaItemsOriginal;

    public ItemAdapter(List<Item> listaItems, Context context) {
        this.listaItems = listaItems;
        this.context=context;
        this.listaItemsOriginal = new ArrayList<>(this.listaItems);
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewNombre;
        public ImageView imageViewIcono;
        public ImageButton Btn_preferits_character;

        public ItemViewHolder(View itemView) {
            super(itemView);
            textViewNombre = itemView.findViewById(R.id.textViewNombre);
            imageViewIcono = itemView.findViewById(R.id.imageViewIcono);
            Btn_preferits_character = itemView.findViewById(R.id.Btn_preferits_character);
        }
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_info, parent, false);
        mFirestore = FirebaseFirestore.getInstance();
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Item item = listaItems.get(position);
        comprovacioEsPreferitDB(item, holder);
        if (item != null) {
            holder.textViewNombre.setText(item.getName());
            String imageUrl = obtenerUrlImagen(item.getSlug());

            if (imageUrl != null && !imageUrl.isEmpty()) {
                Glide.with(holder.imageViewIcono.getContext())
                        .load(imageUrl)
                        .override(200, 200)
                        .centerCrop()
                        .into(holder.imageViewIcono);
            }

            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), DetalleItemActivity.class);
                String itemJson = new Gson().toJson(item);
                intent.putExtra("itemJson", itemJson);
                v.getContext().startActivity(intent);
            });

            // Configuració visual basada en l'estat de preferit de l'item
            if (item.getPreferit()) {
                holder.Btn_preferits_character.setImageResource(R.drawable.btn_star_big_on);
            } else {
                holder.Btn_preferits_character.setImageResource(R.drawable.btn_star_big_off);
            }

            holder.Btn_preferits_character.setOnClickListener(v -> {
                item.setPreferit(!item.getPreferit());

                if (item.getPreferit()) {
                    PreferitsListManager.getInstance().afegirPreferit(item);
                    putClassDB(item, holder);
                    holder.Btn_preferits_character.setImageResource(R.drawable.btn_star_big_on);
                } else {
                    PreferitsListManager.getInstance().eliminarPreferit(item);
                    deleteClassDB(item, holder);
                    holder.Btn_preferits_character.setImageResource(R.drawable.btn_star_big_off);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listaItems.size();
    }

    public void actualizarListaItems(List<Item> nuevaLista) {
        if (nuevaLista == null) {
            nuevaLista = new ArrayList<>(); // Crear una lista vacía si es nula
        }
        listaItems.clear();
        listaItems.addAll(nuevaLista);
        notifyDataSetChanged();
    }

    public static String obtenerUrlImagen(String slug) {
        switch (slug) {
            //espases (2 mans)
            case "the-zweihander": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/unique_sword_2h_002_x1_demonhunter_male.png";
            case "corrupted-ashbringer": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/unique_sword_2h_104_x1_demonhunter_male.png";
            case "scourge": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/unique_sword_2h_004_x1_demonhunter_male.png";
            case "blackguard": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/unique_sword_2h_011_x1_demonhunter_male.png";
            case "the-sultan-of-blinding-sand": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/unique_sword_2h_008_x1_demonhunter_male.png";
            case "blade-of-prophecy": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/p61_unique_sword_2h_007_x1_demonhunter_male.png";
            case "maximus": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/unique_sword_2h_010_x1_demonhunter_male.png";
            case "warmonger": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/unique_sword_2h_003_x1_demonhunter_male.png";
            case "cams-rebuttal": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/unique_sword_2h_102_x1_demonhunter_male.png";
            case "blood-brother": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/unique_sword_2h_103_x1_demonhunter_male.png";
            //destrals (2  mans)
            case "sungjaes-fury": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/transmogaxe_241_004_demonhunter_male.png";
            case "kanais-skorn": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/transmogaxe_241_003_demonhunter_male.png";
            case "burst-of-wrath": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/unique_axe_2h_103_x1_demonhunter_male.png";
            case "messerschmidts-reaver": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/p66_unique_axe_2h_011_demonhunter_male.png";
            case "skorn": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/unique_axe_2h_009_x1_demonhunter_male.png";
            case "decapitator": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/axe_2h_205_demonhunter_male.png";
            case "king-maker": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/transmogaxe_241_002_demonhunter_male.png";
            case "the-executioner": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/p66_unique_axe_2h_003_demonhunter_male.png";
            case "parashu": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/axe_2h_104_demonhunter_male.png";
            case "ripper-axe": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/axe_2h_203_demonhunter_male.png";
            //maces (2 mans)
            case "arthefs-spark-of-life": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/unique_mace_2h_003_x1_demonhunter_male.png";
            case "crushbane": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/unique_mace_2h_001_x1_demonhunter_male.png";
            case "soulsmasher": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/unique_mace_2h_104_x1_demonhunter_male.png";
            case "skywarden": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/unique_mace_2h_010_x1_demonhunter_male.png";
            case "wrath-of-the-bone-king": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/unique_mace_2h_012_p1_demonhunter_male.png";
            case "the-furnace": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/unique_mace_2h_103_x1_demonhunter_male.png";
            case "rock-breaker": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/mace_2h_106_demonhunter_male.png";
            case "war-maul": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/mace_2h_204_demonhunter_male.png";
            case "schaefers-hammer": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/unique_mace_2h_009_p2_demonhunter_male.png";
            case "royal-mace": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/mace_2h_301_demonhunter_male.png";
            //espades (1 mà)
            case "ghoul-kings-blade": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/transmogsword_241_003_demonhunter_male.png";
            case "god-butcher": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/transmogsword_241_001_demonhunter_male.png";
            case "quinquennial-sword": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/transmogsword_241_004_demonhunter_male.png";
            case "amberwing": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/transmogsword_241_002_demonhunter_male.png";
            case "monster-hunter": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/unique_sword_1h_017_x1_demonhunter_male.png";
            case "wildwood": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/unique_sword_1h_002_x1_demonhunter_male.png";
            case "rakanishus-blade": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/unique_sword_1h_010_104_demonhunter_male.png";
            case "the-ancient-bonesaber-of-zumakalis": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/unique_sword_1h_003_x1_demonhunter_male.png";
            case "doombringer": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/unique_sword_1h_014_x1_demonhunter_male.png";
            case "spectrum": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/unique_sword_1h_021_x1_demonhunter_male.png";
            case "thunderfury-blessed-blade-of-the-windseeker": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/unique_sword_1h_101_x1_demonhunter_male.png";
            //Destrals (1 mà)
            case "aidans-revenge": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/transmogaxe_241_001_demonhunter_male.png";
            case "genzaniku": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/unique_axe_1h_003_x1_demonhunter_male.png";
            case "marauder-axe": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/axe_1h_006_demonhunter_male.png";
            case "flesh-tearer": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/unique_axe_1h_001_x1_demonhunter_male.png";
            case "hack": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/unique_axe_1h_103_x1_demonhunter_male.png";
            case "the-butchers-sickle": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/unique_axe_1h_006_x1_demonhunter_male.png";
            case "mordullus-promise": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/p4_unique_axe_1h_102_demonhunter_male.png";
            case "the-burning-axe-of-sankis": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/unique_axe_1h_007_x1_demonhunter_male.png";
            case "sky-splitter": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/unique_axe_1h_005_p2_demonhunter_male.png";
            //dagues
            case "simple-dagger": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/dagger_001_demonhunter_male.png";
            case "envious-blade": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/unique_dagger_103_x1_demonhunter_male.png";
            case "pig-sticker": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/unique_dagger_007_x1_demonhunter_male.png";
            case "the-horadric-hamburger": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/unique_offhand_001_x1_demonhunter_male.png";
            case "wizardspike": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/p610_unique_dagger_010_demonhunter_male.png";
            case "karleis-point": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/p61_unique_dagger_101_x1_demonhunter_male.png";
            case "eunjangdo": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/unique_dagger_104_x1_demonhunter_male.png";
            case "lord-greenstones-fan": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/p61_unique_dagger_102_x1_demonhunter_male.png";
            //vares
            case "the-reapers-kiss": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/transmogstaff_241_001_demonhunter_male.png";
            case "staff-of-chiroptera": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/p61_unique_staff_001_demonhunter_male.png";
            case "long-staff": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/staff_002_demonhunter_male.png";
            case "yew-staff": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/staff_003_demonhunter_male.png";
            case "the-broken-staff": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/unique_staff_001_x1_demonhunter_male.png";
            case "war-staff": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/staff_007_demonhunter_male.png";
            case "the-smoldering-core": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/unique_staff_103_x1_demonhunter_male.png";
            case "suwong-diviner": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/unique_staff_104_x1_demonhunter_male.png";
            case "ahavarion-spear-of-lycander": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/unique_staff_101_x1_demonhunter_male.png";
            case "valtheks-rebuke": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/p610_unique_staff_102_demonhunter_male.png";
            //cascos
            case "mystery-helm": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/ph_helm_demonhunter_male.png";
            case "helm-of-the-cranial-crustacean": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/transmoghelm_002_demonhunter_male.png";
            case "star-helm": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/transmoghelm_001_demonhunter_male.png";
            case "prides-fall": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/unique_helm_103_x1_demonhunter_male.png";
            case "broken-crown": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/p2_unique_helm_001_demonhunter_male.png";
            case "blind-faith": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/unique_helm_007_x1_demonhunter_male.png";
            case "deathseers-cowl": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/unique_helm_102_x1_demonhunter_male.png";
            case "warhelm-of-kassar": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/p4_unique_helm_102_demonhunter_male.png";
            case "visage-of-gunes": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/p4_unique_helm_103_demonhunter_male.png";
            case "mask-of-scarlet-death": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/p6_necro_unique_helm_21_demonhunter_male.png";
            //Hombreres
            case "mystery-shoulders": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/ph_shoulders_demonhunter_male.png";
            case "star-pauldrons": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/transmogshoulders_001_demonhunter_male.png";
            case "leather-mantle": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/shoulders_002_demonhunter_male.png";
            case "pauldrons-of-the-skeleton-king": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/unique_shoulder_103_x1_demonhunter_male.png";
            case "razeths-volition": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/p69_necro_unique_shoulders_22_demonhunter_male.png";
            case "corpsewhisper-pauldrons": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/p6_necro_unique_shoulders_21_demonhunter_male.png";
            case "lefebvres-soliloquy": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/p4_unique_shoulder_101_demonhunter_male.png";
            case "mantle-of-channeling": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/p4_unique_shoulder_103_demonhunter_male.png";
            case "spaulders-of-zakara": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/unique_shoulder_102_x1_demonhunter_male.png";
            case "inariuss-martyrdom": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/p6_necro_set_3_shoulders_demonhunter_male.png";
            //pitxeres
            case "requiem-cereplate": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/p6_necro_unique_chest_22_demonhunter_male.png";
            case "inariuss-conviction": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/p6_necro_set_3_chest_demonhunter_male.png";
            case "pestilence-robe": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/p6_necro_set_4_chest_demonhunter_male.png";
            case "rathmas-ribcage-plate": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/p6_necro_set_1_chest_demonhunter_male.png";
            case "tragouls-scales": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/p6_necro_set_2_chest_demonhunter_male.png";
            case "arachyrs-carapace": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/unique_chest_set_02_p3_demonhunter_male.png";
            case "helltooth-tunic": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/unique_chest_set_16_x1_demonhunter_male.png";
            case "mundunugus-robe": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/p68_unique_chest_set_04_demonhunter_male.png";
            case "typhons-thorax": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/p68_unique_chest_set_03_demonhunter_male.png";
            case "brigandine-of-valor": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/p67_unique_chest_set_01_demonhunter_male.png";
            //guants
            case "gloves-of-worship": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/unique_gloves_103_x1_demonhunter_male.png";
            case "grasps-of-essence": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/p69_necro_unique_gloves_22_demonhunter_male.png";
            case "st-archews-gage": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/unique_gloves_101_p2_demonhunter_male.png";
            case "magefist": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/p41_unique_gloves_014_demonhunter_male.png";
            case "moribund-gauntlets": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/p6_necro_unique_gloves_21_demonhunter_male.png";
            case "gladiator-gauntlets": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/unique_gloves_011_x1_demonhunter_male.png";
            case "manifers": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/gloves_202_demonhunter_male.png";
            case "tal-rashas-grasp": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/p2_unique_gloves_02_demonhunter_male.png";
            case "frostburn": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/p41_unique_gloves_002_demonhunter_male.png";
            case "warlord-gauntlets": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/gloves_203_demonhunter_male.png";
            //pantalons
            case "deaths-bargain": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/unique_pants_102_x1_demonhunter_male.png";
            case "defiler-cuisses": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/p7_necro_unique_pants_22_demonhunter_male.png";
            case "swamp-land-waders": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/p41_unique_pants_001_demonhunter_male.png";
            case "renewal-of-the-invoker": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/unique_pants_set_12_x1_demonhunter_male.png";
            case "sunwukos-leggings": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/unique_pants_set_11_x1_demonhunter_male.png";
            case "arachyrs-legs": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/unique_pants_set_02_p3_demonhunter_male.png";
            case "leg-guards-of-mystery": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/unique_pants_set_02_p2_demonhunter_male.png";
            case "marauders-encasement": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/unique_pants_set_07_x1_demonhunter_male.png";
            case "unholy-plates": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/unique_pants_set_03_p2_demonhunter_male.png";
            case "cold-cathode": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/p69_unique_pants_set_06_demonhunter_male.png";
            //botes
            case "mystery-boots": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/ph_boots_demonhunter_male.png";
            case "lut-socks": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/unique_boots_009_x1_demonhunter_male.png";
            case "the-crudest-boots": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/unique_boots_010_x1_demonhunter_male.png";
            case "rivera-dancers": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/p4_unique_boots_001_demonhunter_male.png";
            case "illusory-boots": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/unique_boots_103_x1_demonhunter_male.png";
            case "boots-of-disregard": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/unique_boots_102_x1_demonhunter_male.png";
            case "irontoe-mudsputters": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/unique_boots_104_x1_demonhunter_male.png";
            case "bryners-journey": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/p6_necro_unique_boots_22_demonhunter_male.png";
            case "fire-walkers": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/unique_boots_007_p2_demonhunter_male.png";
            case "nilfurs-boast-P61_Unique_Boots_01": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/p61_unique_boots_01_demonhunter_male.png";
            //consumibles
            case "health-potion": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/healthpotionbottomless_demonhunter_male.png";
            case "bottomless-potion-of-the-tower": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/healthpotionlegendary_01_demonhunter_male.png";
            case "bottomless-potion-of-the-diamond": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/healthpotionlegendary_02_demonhunter_male.png";
            case "bottomless-potion-of-the-leech": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/healthpotionlegendary_04_demonhunter_male.png";
            case "bottomless-potion-of-fear": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/healthpotionlegendary_08_demonhunter_male.png";
            case "bottomless-potion-of-rejuvenation": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/p2_healthpotionlegendary_07_demonhunter_male.png";
            case "bottomless-potion-of-chaos": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/healthpotionlegendary_10_demonhunter_male.png";
            case "bottomless-potion-of-regeneration": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/healthpotionlegendary_03_demonhunter_male.png";
            case "bottomless-potion-of-mutilation": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/healthpotionlegendary_05_demonhunter_male.png";
            case "bottomless-potion-of-amplification": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/healthpotionlegendary_09_demonhunter_male.png";
            case "bottomless-potion-of-the-unfettered": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/healthpotionlegendary_11_demonhunter_male.png";
            case "bottomless-potion-of-kulleaid": return "https://assets.diablo3.blizzard.com/d3/icons/items/large/healthpotionlegendary_06_demonhunter_male.png";

            default: return "";
        }
    }

    private void putClassDB(Item item, ItemAdapter.ItemViewHolder holder) {
        Map<String, Object> map = new HashMap<>();
        String name = item.getName();
        map.put("name", name);

        mFirestore.collection("Preferits").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(context, "Afegit correctament", Toast.LENGTH_SHORT).show();
                holder.Btn_preferits_character.setImageResource(R.drawable.btn_star_big_on);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Error a l'afegir item", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteClassDB(Item item, ItemAdapter.ItemViewHolder holder) {

        mFirestore.collection("Preferits")
                .whereEqualTo("name", item.getName())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                        // Si es troba el document amb el nom del personatge, eliminar-lo
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            mFirestore.collection("Preferits")
                                    .document(document.getId()) // Obté l'ID del document
                                    .delete()
                                    .addOnSuccessListener(aVoid -> {
                                        // Eliminat amb èxit
                                        Toast.makeText(context, "Personatge eliminat!", Toast.LENGTH_SHORT).show();
                                        //holder.Btn_preferits_character.setImageResource(R.drawable.btn_star_big_off);
                                    })
                                    .addOnFailureListener(e -> {
                                        // Error en eliminar el document
                                        Toast.makeText(context, "Error en eliminar el personatge", Toast.LENGTH_SHORT).show();
                                    });
                        }
                    } else {
                        // Si no es troba cap document amb aquest nom
                        Toast.makeText(context, "No s'ha trobat el personatge", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    // Error en la consulta
                    Toast.makeText(context, "Error en la consulta", Toast.LENGTH_SHORT).show();
                });
    }

    /*private void comprovacioEsPreferitDB(Item item, ItemAdapter.ItemViewHolder holder) {
        PreferitsListManager.getInstance().buidarObjecteLlistaPreferits(Personaje.class);
        mFirestore.collection("Item")
                .whereEqualTo("name", item.getName())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                        item.setPreferit(true);
                        holder.Btn_preferits_character.setImageResource(R.drawable.btn_star_big_on);
                        PreferitsListManager.getInstance().afegirPreferit(item);
                    } else {
                        item.setPreferit(false);
                        holder.Btn_preferits_character.setImageResource(R.drawable.btn_star_big_off);
                    }
                })
                .addOnFailureListener(e -> {
                    // Maneig de l'error en cas que la consulta no funcioni.
                });
    }*/

    private void comprovacioEsPreferitDB(Item item, ItemAdapter.ItemViewHolder holder) {
        PreferitsListManager.getInstance().buidarObjecteLlistaPreferits(Personaje.class);
        mFirestore.collection("Item")
                .whereEqualTo("name", item.getName())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Verificar si l'element està a la base de dades com a preferit
                            Item itemFromDB = document.toObject(Item.class);
                            if (itemFromDB != null && itemFromDB.getPreferit()) {
                                // Si és preferit, actualitza el drawable de l'ImageButton
                                item.setPreferit(true);
                                holder.Btn_preferits_character.setImageResource(R.drawable.btn_star_big_on);
                                PreferitsListManager.getInstance().afegirPreferit(item);
                            } else {
                                // Si no és preferit, actualitza el drawable de l'ImageButton
                                item.setPreferit(false);
                                holder.Btn_preferits_character.setImageResource(R.drawable.btn_star_big_off);
                            }
                        }
                    } else {
                        // Si no es troba cap document amb aquest nom
                        item.setPreferit(false);
                        holder.Btn_preferits_character.setImageResource(R.drawable.btn_star_big_off);
                    }
                })
                .addOnFailureListener(e -> {
                    // Maneig de l'error en cas que la consulta no funcioni.
                });
    }
}

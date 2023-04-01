# KossetuPlugin
EFTライクなデバフや回復アイテムを追加するプラグインです。  
1.16.5~  
多分reload耐性ありません。

## デバフの種類
デバフの効果時間はconfig.ymlで変更できます。
1. 軽出血(1秒ごとに半ハート減ります。50秒持続します)
2. 重出血(1秒ごとに1ハート半減ります。1分30秒持続します)
3. 痛み(走れなくなります。3分持続します)
4. 骨折(治すまで走れなくなります。ジャンプするたびダメージを受けます)

初期ライフだと重出血になった瞬間死が確定すると思うので、3行ほど体力を増やすことを推奨します。

# CustomModelData
ダイヤのクワが基本的な回復アイテムになります。  
1. Car(回復、軽出血直し)  
2. Salewa(回復、軽出血直し)  
3. Immobilizing splint(骨折直し)  
4. CALOK-B(重出血直し)  
5. Golden star balm(鎮痛薬、痛みや骨折のデバフを受けていても走れるようになります)  

# PlaceholderAPI
Placeholderに対応してます。
取得する際の対応表:
出血: LIGHT_BLEEDING
重出血: HEAVY_BLEEDING
骨折 FRACTURE
痛み: PAIN

%kossetu_list% / デバフのリストを返します。  
%kossetu_time_デバフの種類% / デバフの残り時間を返します。  

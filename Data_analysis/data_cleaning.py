import pandas as pd

# Define correct column names based on your dataset
columns = [
    "game_type", "date", "opponent", "tto", "pitcher", "p_hand", "inning", "pitch_count",
    "batter", "b_hand", "rob", "count", "outs", "pa_res", "pitch_type", "pitch_result",
    "pa_result", "base_adv", "exit_velo", "bip_type", "pitch_velo", "ttp", "pa_res_copy",
    "oa3", "putaways", "catcher", "umpire", "pitcher_copy", "team_wl", "score",
    "start_relief", "w", "l", "s", "ip", "r", "er", "lo", "lo_reach", "lo_score",
    "bi", "bi_ch", "sd_inn", "sd_ch", "m_out", "m_ch", "first_responder", "ir",
    "ir_score", "prev_pitch_type", "prev_pitch_result", "prev_pa_result"
]

# Load CSV, skipping the junk row
df = pd.read_csv("cleaned_pitch_data.csv", skiprows=1, names=columns)

# Drop fully empty columns
df = df.dropna(axis=1, how="all")

# Standardize column names: remove spaces, convert to lowercase
df.columns = df.columns.str.strip().str.replace(" ", "_").str.lower()

# Drop duplicate & unnecessary columns
drop_columns = ["pa_res_copy", "pitcher_copy", "prev_pitch_type", "prev_pitch_result", "prev_pa_result"]
df = df.drop(columns=[col for col in drop_columns if col in df.columns])

# Convert numeric columns
numeric_cols = ["inning", "pitch_count", "exit_velo", "pitch_velo", "ttp", "ip", "r", "er", "lo", "lo_reach", "lo_score", "bi", "bi_ch"]
for col in numeric_cols:
    if col in df.columns:
        df[col] = pd.to_numeric(df[col], errors="coerce")

# Save cleaned dataset
df.to_csv("final_cleaned_pitch_data.csv", index=False)

print("\n✅ Data successfully cleaned and saved as 'final_cleaned_pitch_data.csv'.")

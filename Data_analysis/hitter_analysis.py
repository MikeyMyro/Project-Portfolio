import pandas as pd
import os

# Load the cleaned CSV data into a DataFrame
df = pd.read_csv("final_cleaned_pitch_data.csv")
df.columns = df.columns.str.strip().str.replace(" ", "_").str.lower()

# Make sure the CSV has the columns we need
required_cols = ["batter", "exit_velo", "pa_result"]
missing_cols = [col for col in required_cols if col not in df.columns]
if missing_cols:
    print(f"Error: Missing columns {missing_cols}")
    exit()

# Calculate the number of at-bats for each batter (ignoring walks, hit-by-pitches, and sacrifice flies)
at_bats = df[~df["pa_result"].isin(["BB", "HBP", "SF"])].groupby("batter")["pa_result"].count()

# Filter out players with fewer than 10 at-bats to focus on those with enough data
valid_batters = at_bats[at_bats >= 10].index
df = df[df["batter"].isin(valid_batters)]
at_bats = at_bats[at_bats.index.isin(valid_batters)]

# Compute the average exit velocity for each batter
avg_exit_velo = df.groupby("batter")["exit_velo"].mean()

# Find the highest exit velocity recorded for each batter
max_exit_velo = df.groupby("batter")["exit_velo"].max()

# Calculate the percentage of balls hit at 93 mph or higher (hard-hit percentage)
hard_hits = df[df["exit_velo"] >= 93].groupby("batter")["exit_velo"].count()
total_batted_balls = df[df["exit_velo"].notna()].groupby("batter")["exit_velo"].count()
hard_hit_percentage = (hard_hits / total_batted_balls * 100).fillna(0)

# Calculate batting average: (number of hits divided by at-bats)
hits = df[df["pa_result"].isin(["1B", "2B", "3B", "HR"])].groupby("batter")["pa_result"].count().reindex(valid_batters, fill_value=0)
batting_avg = (hits / at_bats).fillna(0)

# Compute on-base percentage (OBP)
walks = df[df["pa_result"] == "BB"].groupby("batter")["pa_result"].count().reindex(valid_batters, fill_value=0)
hit_by_pitch = df[df["pa_result"] == "HBP"].groupby("batter")["pa_result"].count().reindex(valid_batters, fill_value=0)
sac_flies = df[df["pa_result"] == "SF"].groupby("batter")["pa_result"].count().reindex(valid_batters, fill_value=0)
obp_denom = (at_bats + walks + hit_by_pitch + sac_flies).replace(0, 1)  # Prevent division by zero
obp = ((hits + walks + hit_by_pitch) / obp_denom).fillna(0)

# Compute slugging percentage (SLG) by calculating total bases per at-bat
singles = df[df["pa_result"] == "1B"].groupby("batter")["pa_result"].count().reindex(valid_batters, fill_value=0)
doubles = df[df["pa_result"] == "2B"].groupby("batter")["pa_result"].count().reindex(valid_batters, fill_value=0)
triples = df[df["pa_result"] == "3B"].groupby("batter")["pa_result"].count().reindex(valid_batters, fill_value=0)
home_runs = df[df["pa_result"] == "HR"].groupby("batter")["pa_result"].count().reindex(valid_batters, fill_value=0)
total_bases = (singles + (doubles * 2) + (triples * 3) + (home_runs * 4))
slg = (total_bases / at_bats).fillna(0)

# Combine OBP and SLG to get OPS (On-base Plus Slugging)
ops = (obp + slg).fillna(0)

# Calculate strikeout rate as a percentage of total plate appearances
strikeouts = df[df["pa_result"].isin(["KL", "KS", "K"])].groupby("batter")["pa_result"].count().reindex(valid_batters, fill_value=0)
total_pa = df.groupby("batter")["pa_result"].count().reindex(valid_batters, fill_value=0)
strikeout_rate = (strikeouts / total_pa * 100).fillna(0)

# Calculate walk rate as a percentage of total plate appearances
walk_rate = (walks / total_pa * 100).fillna(0)

# Create a new DataFrame that brings together all of the hitting stats
hitting_stats = pd.DataFrame({
    "at_bats": at_bats,                # Total at-bats
    "avg_exit_velo": avg_exit_velo,      # Average exit velocity
    "max_exit_velo": max_exit_velo,      # Maximum exit velocity observed
    "hard_hit_percentage": hard_hit_percentage,
    "batting_avg": batting_avg,          # Batting average (hits/at-bats)
    "obp": obp,                        # On-base percentage
    "slg": slg,                        # Slugging percentage
    "ops": ops,                        # OPS (OBP + SLG)
    "strikeout_rate": strikeout_rate,
    "walk_rate": walk_rate
}).fillna(0)

# Make sure the 'outputs' folder exists
os.makedirs("outputs", exist_ok=True)

# Save the hitting statistics to a CSV file for further analysis or sharing
hitting_stats.to_csv("outputs/hitting_stats.csv")

print("✅ Hitting stats saved to 'outputs/hitting_stats.csv'.")

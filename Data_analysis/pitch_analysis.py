import pandas as pd
import os

# Load the cleaned CSV data into a DataFrame.
df = pd.read_csv("final_cleaned_pitch_data.csv")

# Make sure our data has the essential columns we need.
essential_columns = ["pitcher", "pitch_type", "pitch_velo", "pitch_result", "pa_result", "count", "exit_velo"]
missing = [col for col in essential_columns if col not in df.columns]
if missing:
    print(f"Whoops! The following required columns are missing: {missing}")
    exit()

# Calculate how many pitches each pitcher threw.
pitch_count = df.groupby("pitcher")["pitch_type"].count()

# Only keep pitchers with at least 75 pitches so we have enough data.
df = df[df["pitcher"].isin(pitch_count[pitch_count >= 75].index)]

# Get the average, maximum, and minimum pitch velocities by pitcher and pitch type.
pitch_velo_stats = df.groupby(["pitcher", "pitch_type"])["pitch_velo"].agg(["mean", "max", "min"])

# Figure out the strike percentage: how often did each pitcher throw a strike?
# We consider these types as strikes.
strike_types = ["Str-take", "Str-S/M", "Str-BIP", "Str-foul"]
strike_percentage = (
    df[df["pitch_result"].isin(strike_types)]
    .groupby("pitcher")["pitch_result"]
    .count()
    / df.groupby("pitcher")["pitch_result"].count() * 100
)

# Calculate the percentage of first pitches (count "0-0") that are strikes.
first_pitch_strike = df[df["count"] == "0-0"]
first_pitch_strike_percentage = (
    first_pitch_strike[first_pitch_strike["pitch_result"].isin(strike_types)]
    .groupby("pitcher")["pitch_result"]
    .count()
    / first_pitch_strike.groupby("pitcher")["pitch_result"].count() * 100
)

# Compute the strikeout rate (how many strikeouts as a percentage of total plate appearances).
strikeout_rate = (
    df[df["pa_result"].isin(["KL", "KS", "K"])]
    .groupby("pitcher")["pa_result"]
    .count()
    / df.groupby("pitcher")["pa_result"].count() * 100
)

# Compute the walk rate (walks as a percentage of total plate appearances).
walk_rate = (
    df[df["pa_result"] == "BB"]
    .groupby("pitcher")["pa_result"]
    .count()
    / df.groupby("pitcher")["pa_result"].count() * 100
)

# Calculate the whiff percentage (swings and misses over total swings).
swing_miss = df[df["pitch_result"] == "Str-S/M"]
whiff_percentage = (
    swing_miss.groupby("pitcher")["pitch_result"].count()
    / df[df["pitch_result"].isin(["Str-S/M", "Str-foul"])].groupby("pitcher")["pitch_result"].count()
    * 100
)

# Calculate the hard-hit percentage (balls hit at 93 mph or higher).
hard_hits = (
    df[df["exit_velo"] >= 93].groupby("pitcher")["exit_velo"].count()
    / df[df["exit_velo"].notna()].groupby("pitcher")["exit_velo"].count()
    * 100
)

# Put all these pitching stats together into one DataFrame.
pitching_stats = pd.DataFrame({
    "pitch_count": pitch_count,
    "strike_percentage": strike_percentage,
    "first_pitch_strike_percentage": first_pitch_strike_percentage,
    "strikeout_rate": strikeout_rate,
    "walk_rate": walk_rate,
    "whiff_percentage": whiff_percentage,
    "hard_hit_percentage": hard_hits
}).fillna(0)

# Make sure the 'outputs' folder exists; if not, create it.
os.makedirs("outputs", exist_ok=True)

# Save the overall pitching stats to a CSV file.
pitching_stats.to_csv("outputs/pitching_stats.csv")

# Save the pitch velocity breakdown by pitch type to another CSV file.
pitch_velo_stats.to_csv("outputs/pitch_type_velocity.csv")

print("✅ Pitching stats have been saved to 'outputs/pitching_stats.csv'.")
print("✅ Pitch velocity stats have been saved to 'outputs/pitch_type_velocity.csv'.")

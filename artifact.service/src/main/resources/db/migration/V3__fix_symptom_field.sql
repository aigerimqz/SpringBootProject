ALTER TABLE curse_symptom DROP COLUMN IF EXISTS artifact_id;
ALTER TABLE curse_symptom ADD COLUMN artifact_id UUID REFERENCES cursed_artifacts(id);
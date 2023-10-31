export const tournamentType = ["single platform", "multiplatform"] as const;

export interface Tournament {
  id: number;
  tournamentName: String;
  descprition: String;
  beginDate: Date;
  endDate: Date;
  tournamentType: typeof tournamentType;
}

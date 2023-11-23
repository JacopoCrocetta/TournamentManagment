import { Tournament } from "src/core/entity/abstract-tournament";

export namespace TournamentPageMapper {
  export const tournamentPageItemMapper = (input: Tournament) => ({
    id: input.id,
    tournamentName: input.tournamentName,
    descprition: input.descprition,
    beginDate: input.beginDate,
    endDate: input.endDate,
    tournamentType: input.tournamentType,
  });

  export interface TournamentPageItemMapper {
    tournamentName: String;
    descprition: String;
    beginDate: Date;
    endDate: Date;
    tournamentType: string;
  }
}

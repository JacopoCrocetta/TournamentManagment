import { Tournament } from "src/core/entity/abstract-tournament";

export namespace TournamentPageMapper {
  export const tournamentMapper = (input: Tournament) => ({
    id: input.id,
    tournamentName: input.tournamentName,
    descprition: input.descprition,
    beginDate: input.beginDate,
    endDate: input.endDate,
    tournamentType: input.tournamentType,
  });

  export interface TournamentPageItemMapper {
    tournamentName: string;
    descprition: string;
    beginDate: Date;
    endDate: Date;
    tournamentType: string;
  }
}

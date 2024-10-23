import { PrismaService } from "src/infrastructure/database/prisma.service";
import { TournamentsBaseRepository } from "./tournaments-base.repository";
import { ITournamentsRepository } from "src/core/repository/tournaments.repository";
import { Tournament } from "src/core/entity/abstract-tournament";

export class TournamentsInfrastructureRepository extends TournamentsBaseRepository implements ITournamentsRepository {
  getById(id: string) {
    throw new Error("Method not implemented.");
  }
  getAll() {
    throw new Error("Method not implemented.");
  }
  create(matchToCreate: Tournament) {
    throw new Error("Method not implemented.");
  }
  update(matchToUpdate: Tournament) {
    throw new Error("Method not implemented.");
  }
  delete(id: string) {
    throw new Error("Method not implemented.");
  }
}

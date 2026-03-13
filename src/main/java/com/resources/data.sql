-- SkillMentor Seed Data

-- Insert mentors (only if table is empty)
INSERT INTO mentors (id, first_name, last_name, email, phone, title, profession, company, experience_years, bio, profile_image_url, is_certified, start_year, rating, review_count, total_students, created_at)
SELECT 1, 'Sarah', 'Chen', 'sarah.chen@example.com', '+1 555-0101', 'Senior Software Engineer', 'Software Engineering', 'Google', 12,
       'Passionate about teaching full-stack development with a focus on React, TypeScript, and cloud architecture. I''ve mentored over 200 students and helped them land roles at top tech companies.',
       'https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=400&h=400&fit=crop&crop=face', true, 2018, 4.9, 156, 234, NOW()
    WHERE NOT EXISTS (SELECT 1 FROM mentors WHERE id = 1);

INSERT INTO mentors (id, first_name, last_name, email, phone, title, profession, company, experience_years, bio, profile_image_url, is_certified, start_year, rating, review_count, total_students, created_at)
SELECT 2, 'Marcus', 'Williams', 'marcus.w@example.com', '+1 555-0102', 'Data Science Lead', 'Data Science', 'Netflix', 9,
       'Data scientist with expertise in machine learning, NLP, and recommendation systems. I enjoy breaking down complex ML concepts into intuitive lessons.',
       'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=400&h=400&fit=crop&crop=face', true, 2020, 4.8, 98, 178, NOW()
    WHERE NOT EXISTS (SELECT 1 FROM mentors WHERE id = 2);

INSERT INTO mentors (id, first_name, last_name, email, phone, title, profession, company, experience_years, bio, profile_image_url, is_certified, start_year, rating, review_count, total_students, created_at)
SELECT 3, 'Emily', 'Rodriguez', 'emily.r@example.com', '+1 555-0103', 'UX Design Director', 'UX/UI Design', 'Figma', 11,
       'Design leader focused on creating intuitive, accessible user experiences. My students learn not just tools, but how to think like a designer.',
       'https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=400&h=400&fit=crop&crop=face', true, 2019, 4.9, 134, 198, NOW()
    WHERE NOT EXISTS (SELECT 1 FROM mentors WHERE id = 3);

INSERT INTO mentors (id, first_name, last_name, email, phone, title, profession, company, experience_years, bio, profile_image_url, is_certified, start_year, rating, review_count, total_students, created_at)
SELECT 4, 'James', 'Park', 'james.park@example.com', '+1 555-0104', 'DevOps Architect', 'DevOps Engineering', 'Spotify', 8,
       'Infrastructure and DevOps specialist helping teams ship faster with CI/CD, Kubernetes, and infrastructure as code.',
       'https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=400&h=400&fit=crop&crop=face', false, 2021, 4.7, 72, 120, NOW()
    WHERE NOT EXISTS (SELECT 1 FROM mentors WHERE id = 4);

INSERT INTO mentors (id, first_name, last_name, email, phone, title, profession, company, experience_years, bio, profile_image_url, is_certified, start_year, rating, review_count, total_students, created_at)
SELECT 5, 'Aisha', 'Patel', 'aisha.p@example.com', '+1 555-0105', 'Mobile Engineering Manager', 'Mobile Development', 'Stripe', 10,
       'Specialist in cross-platform mobile development with React Native and Flutter. I focus on teaching best practices for building performant mobile applications.',
       'https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=400&h=400&fit=crop&crop=face', true, 2019, 4.8, 110, 165, NOW()
    WHERE NOT EXISTS (SELECT 1 FROM mentors WHERE id = 5);

-- Insert subjects
INSERT INTO subjects (id, name, description, image_url, enrollment_count, mentor_id, created_at)
SELECT 1, 'React & TypeScript', 'Modern frontend development with React 18, TypeScript, and state management patterns.',
       'https://images.unsplash.com/photo-1633356122544-f134324a6cee?w=400&h=250&fit=crop', 89, 1, NOW()
    WHERE NOT EXISTS (SELECT 1 FROM subjects WHERE id = 1);

INSERT INTO subjects (id, name, description, image_url, enrollment_count, mentor_id, created_at)
SELECT 2, 'System Design', 'Learn to design scalable distributed systems used at top tech companies.',
       'https://images.unsplash.com/photo-1551288049-bebda4e38f71?w=400&h=250&fit=crop', 67, 1, NOW()
    WHERE NOT EXISTS (SELECT 1 FROM subjects WHERE id = 2);

INSERT INTO subjects (id, name, description, image_url, enrollment_count, mentor_id, created_at)
SELECT 3, 'Cloud Architecture', 'AWS, GCP, and Azure fundamentals for building production-grade applications.',
       'https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=400&h=250&fit=crop', 45, 1, NOW()
    WHERE NOT EXISTS (SELECT 1 FROM subjects WHERE id = 3);

INSERT INTO subjects (id, name, description, image_url, enrollment_count, mentor_id, created_at)
SELECT 4, 'Machine Learning', 'From linear regression to deep learning — build and deploy ML models.',
       'https://images.unsplash.com/photo-1555949963-aa79dcee981c?w=400&h=250&fit=crop', 112, 2, NOW()
    WHERE NOT EXISTS (SELECT 1 FROM subjects WHERE id = 4);

INSERT INTO subjects (id, name, description, image_url, enrollment_count, mentor_id, created_at)
SELECT 5, 'Python for Data Science', 'Master pandas, NumPy, and scikit-learn for data analysis and modeling.',
       'https://images.unsplash.com/photo-1526374965328-7f61d4dc18c5?w=400&h=250&fit=crop', 94, 2, NOW()
    WHERE NOT EXISTS (SELECT 1 FROM subjects WHERE id = 5);

INSERT INTO subjects (id, name, description, image_url, enrollment_count, mentor_id, created_at)
SELECT 6, 'UI/UX Fundamentals', 'Core principles of user interface and experience design for digital products.',
       'https://images.unsplash.com/photo-1561070791-2526d30994b5?w=400&h=250&fit=crop', 76, 3, NOW()
    WHERE NOT EXISTS (SELECT 1 FROM subjects WHERE id = 6);

INSERT INTO subjects (id, name, description, image_url, enrollment_count, mentor_id, created_at)
SELECT 7, 'Figma Mastery', 'Advanced Figma techniques including auto-layout, components, and prototyping.',
       'https://images.unsplash.com/photo-1609921212029-bb5a28e60960?w=400&h=250&fit=crop', 88, 3, NOW()
    WHERE NOT EXISTS (SELECT 1 FROM subjects WHERE id = 7);

INSERT INTO subjects (id, name, description, image_url, enrollment_count, mentor_id, created_at)
SELECT 8, 'Design Systems', 'Build scalable design systems that bridge the gap between design and development.',
       'https://images.unsplash.com/photo-1558655146-9f40138edfeb?w=400&h=250&fit=crop', 52, 3, NOW()
    WHERE NOT EXISTS (SELECT 1 FROM subjects WHERE id = 8);

INSERT INTO subjects (id, name, description, image_url, enrollment_count, mentor_id, created_at)
SELECT 9, 'Docker & Kubernetes', 'Container orchestration from beginner to production-ready deployments.',
       'https://images.unsplash.com/photo-1667372393119-3d4c48d07fc9?w=400&h=250&fit=crop', 65, 4, NOW()
    WHERE NOT EXISTS (SELECT 1 FROM subjects WHERE id = 9);

INSERT INTO subjects (id, name, description, image_url, enrollment_count, mentor_id, created_at)
SELECT 10, 'CI/CD Pipelines', 'Automate your deployment workflow with GitHub Actions, Jenkins, and GitLab CI.',
       'https://images.unsplash.com/photo-1618401471353-b98afee0b2eb?w=400&h=250&fit=crop', 43, 4, NOW()
    WHERE NOT EXISTS (SELECT 1 FROM subjects WHERE id = 10);

INSERT INTO subjects (id, name, description, image_url, enrollment_count, mentor_id, created_at)
SELECT 11, 'React Native', 'Build production-ready iOS and Android apps with React Native and Expo.',
       'https://images.unsplash.com/photo-1512941937669-90a1b58e7e9c?w=400&h=250&fit=crop', 98, 5, NOW()
    WHERE NOT EXISTS (SELECT 1 FROM subjects WHERE id = 11);

INSERT INTO subjects (id, name, description, image_url, enrollment_count, mentor_id, created_at)
SELECT 12, 'Mobile UI Patterns', 'Navigation, gestures, and animations that feel native on both platforms.',
       'https://images.unsplash.com/photo-1551650975-87deedd944c3?w=400&h=250&fit=crop', 54, 5, NOW()
    WHERE NOT EXISTS (SELECT 1 FROM subjects WHERE id = 12);

-- Reset sequences
SELECT setval('mentors_id_seq', (SELECT MAX(id) FROM mentors));
SELECT setval('subjects_id_seq', (SELECT MAX(id) FROM subjects));
